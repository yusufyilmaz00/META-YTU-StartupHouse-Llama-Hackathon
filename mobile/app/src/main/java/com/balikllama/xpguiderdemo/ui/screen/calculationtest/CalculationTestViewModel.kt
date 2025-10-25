package com.balikllama.xpguiderdemo.ui.screen.calculationtest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balikllama.xpguiderdemo.data.local.entity.AnswerType
import com.balikllama.xpguiderdemo.domain.ScoreCalculator
import com.balikllama.xpguiderdemo.domain.ScoreResult
import com.balikllama.xpguiderdemo.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

// Önceden tanımlanmış test senaryomuz.
// Soru ID'si -> Cevap
val PREDEFINED_ANSWERS: Map<String, AnswerType> = mapOf(
    "Q1" to AnswerType.YES,       // Analitik
    "Q2" to AnswerType.YES,       // Yaratıcı
    "Q3" to AnswerType.YES,       // Ruhsal
    "Q4" to AnswerType.NO,        // Fiziksel (negatif etki)
    "Q5" to AnswerType.NEUTRAL,   // Dilsel (hafif pozitif etki)
    "Q6" to AnswerType.YES,       // Pratik
    "Q7" to AnswerType.YES,       // Doğal
    "Q8" to AnswerType.YES,       // Dilsel
    "Q9" to AnswerType.YES,       // Yaratıcı
    "Q10" to AnswerType.YES,      // Duygusal
    "Q11" to AnswerType.NO,       // Fiziksel (negatif etki)
    "Q12" to AnswerType.YES,      // Sosyal
    "Q13" to AnswerType.YES,      // İletişimsel
    "Q14" to AnswerType.YES,      // Bilimsel
    "Q15" to AnswerType.YES,      // Duygusal
    "Q16" to AnswerType.NEUTRAL,  // Stratejik (hafif pozitif etki)
    "Q17" to AnswerType.YES,      // Girişimcilik
    "Q18" to AnswerType.NO,       // Sosyal (negatif etki)
    "Q19" to AnswerType.YES,      // Dijital
    "Q20" to AnswerType.YES       // Doğal
    // Bu listeyi istediğiniz gibi genişletip farklı senaryoları test edebilirsiniz.
)

data class CalculationTestUIState(
    val isLoading: Boolean = true,
    val results: List<ScoreResult> = emptyList(),
    val isCalculationComplete: Boolean = false
)

@HiltViewModel
class CalculationTestViewModel @Inject constructor(
    private val testRepository: TestRepository,
    private val scoreCalculator: ScoreCalculator
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculationTestUIState())
    val uiState = _uiState.asStateFlow()

    fun runPredefinedTest() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isCalculationComplete = false) }

            // 1. Sahte cevaplar için benzersiz bir test seans ID'si oluştur.
            val fakeTestSessionId = "predefined-test-${UUID.randomUUID()}"

            // 2. Önceden tanımlanmış cevapları veritabanına kaydet.
            PREDEFINED_ANSWERS.forEach { (questionId, answer) ->
                testRepository.saveOrUpdateAnswer(
                    testSessionId = fakeTestSessionId,
                    questionId = questionId,
                    answer = answer
                )
            }

            // 3. ScoreCalculator'ı bu sahte seans ID'si ile çalıştır.
            val calculatedResults = scoreCalculator.calculateScores(fakeTestSessionId)

            // 4. Sonuçları ve hesaplamanın bittiğini UI'a bildir.
            _uiState.update {
                it.copy(
                    isLoading = false,
                    results = calculatedResults,
                    isCalculationComplete = true
                )
            }
        }
    }
}
