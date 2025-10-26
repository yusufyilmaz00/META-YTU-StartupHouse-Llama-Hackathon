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

// ---------------------- Yardımcı: String -> List<AnswerType> ----------------------
private fun parseAnswers(input: String): List<AnswerType> {
    return input.split(",")
        .map { it.trim().uppercase() } // " k " -> "K"
        .map { letter ->
            when (letter) {
                "E" -> AnswerType.YES
                "K" -> AnswerType.NEUTRAL
                "H" -> AnswerType.NO
                else -> error("Unknown answer code: $letter")
            }
        }
}

// ---------------------- Varsayılan test string'i ----------------------
private const val DEFAULT_ANSWER_STRING =
    "E,H,E,H,E,E,E,E,E,E,E,H,E,E,E,E,E,H,E,K,H,K,E,H,E,E,H,H,H,E,E,K,H,E,H,K,K,E,E,E,E,E,E,H,H,K,E,E"

// Önceden tanımlanmış test senaryomuz (string'ten otomatik üretilir)
val PREDEFINED_ANSWERS: List<AnswerType> = parseAnswers(DEFAULT_ANSWER_STRING)

// ---------------------- UI State ----------------------
data class CalculationTestUIState(
    val isLoading: Boolean = true,
    val results: List<ScoreResult> = emptyList(),
    val isCalculationComplete: Boolean = false
)

// ---------------------- ViewModel ----------------------
@HiltViewModel
class CalculationTestViewModel @Inject constructor(
    private val testRepository: TestRepository,
    private val scoreCalculator: ScoreCalculator
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculationTestUIState())
    val uiState = _uiState.asStateFlow()

    /**
     * Varsayılan (DEFAULT_ANSWER_STRING) string ile koşar.
     */
    fun runPredefinedTest() = runPredefinedTest(null)

    /**
     * İstersen farklı bir cevap dizisini string olarak verip test çalıştırabilirsin.
     * Örn: "K,E,H,..." biçiminde.
     */
    fun runPredefinedTest(input: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isCalculationComplete = false) }

            val answers: List<AnswerType> = if (input.isNullOrBlank()) {
                PREDEFINED_ANSWERS
            } else {
                parseAnswers(input)
            }

            // 1) Benzersiz test seansı
            val fakeTestSessionId = "predefined-test-${UUID.randomUUID()}"

            // 2) Cevapları DB'ye yaz
            answers.forEachIndexed { index, answer ->
                val questionId = "Q${index + 1}"
                testRepository.saveOrUpdateAnswer(
                    testSessionId = fakeTestSessionId,
                    questionId = questionId,
                    answer = answer
                )
            }

            // 3) Skor hesapla
            val calculatedResults = scoreCalculator.calculateScores(fakeTestSessionId)

            // 4) UI güncelle
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
