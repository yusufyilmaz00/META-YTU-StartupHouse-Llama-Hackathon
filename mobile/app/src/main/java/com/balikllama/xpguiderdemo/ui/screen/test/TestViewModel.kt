package com.balikllama.xpguiderdemo.ui.screen.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balikllama.xpguiderdemo.data.local.entity.AnswerType
import com.balikllama.xpguiderdemo.domain.ScoreCalculator
import com.balikllama.xpguiderdemo.domain.ScoreResult
import com.balikllama.xpguiderdemo.repository.CreditRepository
import com.balikllama.xpguiderdemo.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import android.util.Log
import com.balikllama.xpguiderdemo.data.local.entity.TestResult
import com.balikllama.xpguiderdemo.repository.ChatbotRepository
import com.balikllama.xpguiderdemo.repository.TestResultRepository
import com.balikllama.xpguiderdemo.ui.screen.testresult.TraitResult

private const val DEFAULT_ANSWER_STRING =
    "E,H,E,H,E,E,E,E,E,E,E,H,E,E,E,E,E,H,E,K,H,K,E,H,E,E,H,H,H,E,E,K,H,E,H,K,K,E,E,E,E,E,E,H,H,K,E,E"

private fun parseAnswers(input: String): List<AnswerType> {
    return input.split(",")
        .map { it.trim().uppercase() }
        .map { letter ->
            when (letter) {
                "E" -> AnswerType.YES
                "K" -> AnswerType.NEUTRAL
                "H" -> AnswerType.NO
                else -> error("Unknown answer code: $letter")
            }
        }
}

@HiltViewModel
class TestViewModel @Inject constructor(
    private val testRepository: TestRepository,
    private val creditRepository: CreditRepository,
    private val scoreCalculator: ScoreCalculator,
    private val testResultRepository: TestResultRepository,
    private val chatbotRepository: ChatbotRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TestUIState())

    // Kredi ve test durumunu birleştirerek tek bir UIState oluştur
    val uiState: StateFlow<TestUIState> = combine(
        _uiState,
        creditRepository.userCredits
    ) { testState, credit ->
        testState.copy(credit = credit)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TestUIState()
    )

    // Test boyunca aynı kalacak olan benzersiz seans kimliği.
    // Bu sayede farklı zamanlarda yapılan testlerin cevapları birbirine karışmaz.
    private var testSessionId: String = ""
    // Test tamamlandığında hesaplanan skorları tutacak yeni bir state ekleyelim.
    private val _testResults = MutableStateFlow<List<ScoreResult>>(emptyList())

    init {
        startNewTest()
    }
    /**
     * Bir sonraki soruya ilerler veya test bittiyse 'isTestCompleted' durumunu true yapar.
     */
    private fun goToNextQuestion() {
        viewModelScope.launch { // Launch coroutine for potential score calculation
            val nextIndex = _uiState.value.currentQuestionIndex + 1
            if (nextIndex < _uiState.value.questions.size) {
                _uiState.update { it.copy(currentQuestionIndex = nextIndex) }
                loadAnswerForCurrentQuestion()
            } else {
                _uiState.update {
                    it.copy(isTestFinishedButNotSubmitted = true)
                }
            }
        }
    }

    /**
     * YENİ FONKSİYON: Testi bitirir, sonuçları kaydeder, API'ye gönderir.
     * Bu fonksiyon UI tarafından (yeni butona basılınca) çağrılacak.
     */
    fun submitTestResults() {
        viewModelScope.launch {
            // --- LOGLAMA ---
            Log.d("TestViewModel", "submitTestResults çağrıldı. Skorlar hesaplanıyor...")
            val results = scoreCalculator.calculateScores(testSessionId)
            Log.d("TestViewModel", "Skorlar hesaplandı: $results")

            if (results.isEmpty()) {
                Log.e("TestViewModel", "Hesaplanan sonuçlar boş, işlem iptal edildi.")
                return@launch
            }

            // A) Sonuçları veritabanına kaydet
            val testResultsToSave = results.map { score ->
                TestResult(name = score.traitName, percentage = score.displayPercent)
            }
            testResultRepository.saveTestResults(testResultsToSave)
            Log.d("TestViewModel", "Test sonuçları veritabanına kaydedildi.")

            // B) API'ye gönder
            val ratiosMap = testResultsToSave.associate { it.name to it.percentage.toDouble() }
            chatbotRepository.sendRatiosToIntelligenceApi(ratiosMap)
            Log.d("TestViewModel", "API isteği gönderildi: $ratiosMap")

            // C) UI'ı son durumla güncelle (sonuç ekranı için)
            _uiState.update {
                it.copy(
                    isTestCompleted = true, // Testin tamamen bittiğini belirt
                    results = results,
                    isTestFinishedButNotSubmitted = false // Artık gönderildiği için bu durumu false yap
                )
            }
        }
    }

    /**
     * Yeni bir test seansı başlatır, soruları veritabanından çeker.
     */
    private fun startNewTest() {
        // Her yeni test başlangıcında benzersiz bir kimlik oluştur.
        testSessionId = UUID.randomUUID().toString()
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val questions = testRepository.getQuestions()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    questions = questions,
                    isTestCompleted = false,
                    currentQuestionIndex = 0
                )
            }
            // Başlangıç olarak ilk sorunun daha önce verilmiş bir cevabı var mı diye kontrol et.
            // Bu, kullanıcı bir testi yarım bırakıp dönerse diye bir senaryo için hazırlık,
            // ama bizim senaryomuzda hep boş gelecektir.
            loadAnswerForCurrentQuestion()
        }
    }

    /**
     * Kullanıcı "Evet", "Hayır" veya "Kararsızım" butonlarından birine bastığında çağrılır.
     */
    fun onAnswerSelected(answer: AnswerType) {
        val currentQuestion = _uiState.value.currentQuestion ?: return

        viewModelScope.launch {
            testRepository.saveOrUpdateAnswer(
                testSessionId = testSessionId,
                questionId = currentQuestion.qId,
                answer = answer
            )
            _uiState.update { it.copy(currentAnswer = answer) }
            goToNextQuestion()
        }
    }

    /**
     * Geri butonuna basıldığında bir önceki soruya döner.
     */
    fun goToPreviousQuestion() {
        val prevIndex = _uiState.value.currentQuestionIndex - 1
        if (prevIndex >= 0) {
            _uiState.update { it.copy(currentQuestionIndex = prevIndex) }
            // Geri dönülen sorunun cevabını yükle ki UI'da doğru buton seçili görünsün.
            loadAnswerForCurrentQuestion()
        }
    }

    /**
     * Ekranda gösterilen mevcut soruya bu seans içinde daha önce cevap verilip verilmediğini
     * veritabanından kontrol eder ve UI durumunu günceller.
     */
    private fun loadAnswerForCurrentQuestion() {
        val currentQuestion = _uiState.value.currentQuestion ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(currentAnswer = null) }

            val existingAnswer = testRepository.getAnswerForQuestion(
                testSessionId = testSessionId,
                questionId = currentQuestion.qId
            )
            _uiState.update { it.copy(currentAnswer = existingAnswer?.answer) }
        }
    }

    /**
     * Test sonuçlarını dışarıdan (örneğin bir test senaryosundan) ayarlamak için kullanılır.
     */
    fun setTestResults(results: List<ScoreResult>) {
        _uiState.update {
            it.copy(
                results = results,
                isTestCompleted = true
            )
        }
    }
}
