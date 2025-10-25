package com.balikllama.xpguiderdemo.domain

import com.balikllama.xpguiderdemo.data.local.entity.AnswerType
import com.balikllama.xpguiderdemo.repository.CalculationFactorRepository
import com.balikllama.xpguiderdemo.repository.TestRepository
import com.balikllama.xpguiderdemo.repository.TraitRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

// Sonuçları tutacak veri sınıfı
data class ScoreResult(
    val traitId: String,
    val traitName: String,
    val score: Float
)

/**
 * Test sonuçlarını hesaplamak için kullanılan sınıf.
 * Gerekli tüm veri havuzlarını (repository) alarak ham veriyi işler.
 */
class ScoreCalculator @Inject constructor(
    private val traitRepository: TraitRepository,
    private val calculationFactorRepository: CalculationFactorRepository,
    private val testRepository: TestRepository
) {
    /**
     * Belirtilen test seansının sonuçlarını hesaplar.
     * @param testSessionId Hesaplamanın yapılacağı testin kimliği.
     * @return Özellik ID'si, adı ve hesaplanmış skoru içeren bir liste döner.
     */
    suspend fun calculateScores(testSessionId: String): List<ScoreResult> {
        // 1. Gerekli verileri veritabanından tek seferde çekelim.
        val allTraits = traitRepository.getAllTraits().first()
        val factors = calculationFactorRepository.getAllFactors().first().associate { it.key to it.value }
        val allQuestions = testRepository.getQuestions()
        val userAnswers = testRepository.getAllAnswersForSession(testSessionId)

        // Hesaplama için gerekli çarpanları alalım. Eksikse varsayılan değerler kullanalım.
        val yesFactor = factors["answer_yes"] ?: 1.0f
        val maybeFactor = factors["answer_maybe"] ?: 0.2f
        val noFactor = factors["answer_no"] ?: -0.4f
        val primaryWeight = factors["weight_primary"] ?: 2.0f
        val secondaryWeight = factors["weight_secondary"] ?: 0.1f

        // 2. Özellik (trait) ID'lerine göre skorları tutacak bir harita (map) oluşturalım.
        val scores = allTraits.associate { it.traitId to 0f }.toMutableMap()

        // 3. Her bir kullanıcı cevabı için skorları hesaplayıp güncelleyelim.
        for (answer in userAnswers) {
            val question = allQuestions.find { it.qId == answer.questionId } ?: continue

            val multiplier = when (answer.answer) {
                AnswerType.YES -> yesFactor
                AnswerType.NEUTRAL -> maybeFactor
                AnswerType.NO -> noFactor
            }

            // Birincil özellik (primary trait) skorunu güncelle
            if (scores.containsKey(question.primaryId)) {
                scores[question.primaryId] = scores.getValue(question.primaryId) + (primaryWeight * multiplier)
            }

            // İkincil özellik 1 (secondary 1) skorunu güncelle
            if (question.s1Id.isNotBlank() && scores.containsKey(question.s1Id)) {
                scores[question.s1Id] = scores.getValue(question.s1Id) + (secondaryWeight * question.s1w * multiplier)
            }

            // İkincil özellik 2 (secondary 2) skorunu güncelle
            if (question.s2Id.isNotBlank() && scores.containsKey(question.s2Id)) {
                scores[question.s2Id] = scores.getValue(question.s2Id) + (secondaryWeight * question.s2w * multiplier)
            }

            // İkincil özellik 3 (secondary 3) skorunu güncelle
            if (question.s3Id.isNotBlank() && scores.containsKey(question.s3Id)) {
                scores[question.s3Id] = scores.getValue(question.s3Id) + (secondaryWeight * question.s3w * multiplier)
            }
        }

        // 4. Sonuçları daha anlaşılır bir formata dönüştürelim (ID yerine isimleri de ekleyelim).
        return scores.map { (traitId, score) ->
            ScoreResult(
                traitId = traitId,
                traitName = allTraits.find { it.traitId == traitId }?.traitName ?: "Bilinmeyen Özellik",
                score = score
            )
        }.sortedByDescending { it.score } // Skorları büyükten küçüğe sırala
    }
}
