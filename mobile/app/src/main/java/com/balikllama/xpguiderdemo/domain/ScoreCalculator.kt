package com.balikllama.xpguiderdemo.domain

import android.util.Log
import com.balikllama.xpguiderdemo.data.local.entity.AnswerType
import com.balikllama.xpguiderdemo.repository.CalculationFactorRepository
import com.balikllama.xpguiderdemo.repository.TestRepository
import com.balikllama.xpguiderdemo.repository.TraitRepository
import kotlinx.coroutines.flow.first
import kotlin.math.pow
import java.util.Locale
import javax.inject.Inject

data class SecondaryWeight(
    val traitId: String,
    val weight: Float
)

data class QuestionNorm(
    val id: String,
    val primaryTraitId: String,
    val secondaries: List<SecondaryWeight>,
    val active: Boolean
)

data class ScoreResult(
    val traitId: String,
    val traitName: String,
    val raw: Float,
    val max: Float,
    val fairPercent: Float,
    val softmaxPercent: Float,
    val displayPercent: Float,
    val score: Float = displayPercent
)

enum class Answer { NO, MAYBE, YES }

class ScoreCalculator @Inject constructor(
    private val traitRepository: TraitRepository,
    private val calculationFactorRepository: CalculationFactorRepository,
    private val testRepository: TestRepository
) {
    // Jitter KAPALI: Unity ile birebir kıyas için
    private val enableJitter: Boolean = false

    private val KEY_YES = "answer_yes"
    private val KEY_MAYBE = "answer_maybe"
    private val KEY_NO = "answer_no"
    private val KEY_W_PRIMARY = "weight_primary"
    private val KEY_W_SECONDARY = "weight_secondary"

    suspend fun calculateScores(testSessionId: String): List<ScoreResult> {
        // 1) Veriler
        val traits = traitRepository.getAllTraits().first()
        val factors = calculationFactorRepository.getAllFactors().first().associate { it.key to it.value }
        val questionsRaw = testRepository.getQuestions()
        val answersRaw = testRepository.getAllAnswersForSession(testSessionId)

        // 2) Faktörler (Inspector/DB > varsayılan)
        val yesFactor = factors[KEY_YES] ?: 1.0f
        val maybeFactor = factors[KEY_MAYBE] ?: 0.2f
        val noFactor = factors[KEY_NO] ?: -0.4f
        val primaryMultiplier = factors[KEY_W_PRIMARY] ?: 2.0f
        val secondaryMultiplier = factors[KEY_W_SECONDARY] ?: 0.1f

        Log.i("ScoreCalculator", "FACTORS yes=$yesFactor maybe=$maybeFactor no=$noFactor primary=$primaryMultiplier secondary=$secondaryMultiplier")

        // --- AĞIRLIK PARSE (güçlü) ---
        fun parseWeightLoose(w: Any?): Float {
            if (w == null) return 0f
            var s = w.toString().trim()
            val hasPercent = s.contains('%')
            s = s.replace("%", "")
            s = s.replace(',', '.')       // "0,1" -> "0.1"
            s = s.filter { it.isDigit() || it == '.' || it == '-' } // sadece sayı/nokta/işaret kalsın
            val v = s.toFloatOrNull() ?: 0f
            return if (hasPercent) v / 100f else v
        }

        // 3) s1/s2/s3 -> List<SecondaryWeight> (ID trim + 0 ağırlık DA eklenir)
        val questions: List<QuestionNorm> = questionsRaw.map { qr ->
            fun addEvenIfZero(id: String?, w: Any?, acc: MutableList<SecondaryWeight>) {
                val tid = (id ?: "").trim()
                if (tid.isNotEmpty()) acc.add(SecondaryWeight(tid, parseWeightLoose(w)))
            }
            val secs = mutableListOf<SecondaryWeight>()
            addEvenIfZero(qr.s1Id, qr.s1w, secs)
            addEvenIfZero(qr.s2Id, qr.s2w, secs)
            addEvenIfZero(qr.s3Id, qr.s3w, secs)

            val qId = (qr.qId ?: "").trim()
            val pId = (qr.primaryId ?: "").trim()
            val active = try {
                val f = qr::class.members.firstOrNull { it.name == "active" }?.call(qr) as? Boolean
                f ?: true
            } catch (_: Throwable) { true }

            QuestionNorm(
                id = qId,
                primaryTraitId = pId,
                secondaries = secs,
                active = active
            )
        }

        // 4) Cevaplar (same questionId -> SONUNCU). ID/TRIM fix
        val answers: Map<String, Answer> = answersRaw
            .groupBy { (it.questionId ?: "").trim() }
            .mapValues { (_, list) ->
                when (list.last().answer) {
                    AnswerType.YES -> Answer.YES
                    AnswerType.NEUTRAL -> Answer.MAYBE
                    AnswerType.NO -> Answer.NO
                }
            }

        // *** Sadece CEVAPLANAN sorularla ilerle ***
        val answeredIds = answers.keys
        val questionsUsed = questions.filter { it.active && it.id.isNotEmpty() && it.id in answeredIds }

        // Secondary durumunu teşhis et (say ve örnekle)
        val secTotal = questionsUsed.sumOf { it.secondaries.size }
        val secZero  = questionsUsed.sumOf { it.secondaries.count { sw -> sw.weight == 0f } }
        Log.i("ScoreCalculator", "SECONDARIES -> total=$secTotal, zeroWeights=$secZero")
        questionsUsed.take(5).forEach { q ->
            val secs = q.secondaries.joinToString { "${it.traitId}:${"%.3f".format(Locale.US, it.weight)}" }
            Log.i("ScoreCalculator", "Q=${q.id} P=${q.primaryTraitId} secs=[$secs]")
        }

        Log.i("ScoreCalculator", "QuestionsUsed=${questionsUsed.size}, Answers=${answers.size}")

        // 5) scoreRaw
        val rawMap: MutableMap<String, Float> = mutableMapOf()
        for (q in questionsUsed) {
            rawMap.putIfAbsent(q.primaryTraitId, 0f)
            q.secondaries.forEach { rawMap.putIfAbsent(it.traitId, 0f) }
        }
        for (q in questionsUsed) {
            val a = answers[q.id] ?: continue
            val m = when (a) {
                Answer.YES -> yesFactor
                Answer.MAYBE -> maybeFactor
                Answer.NO -> noFactor
            }
            rawMap[q.primaryTraitId] = rawMap[q.primaryTraitId]!! + primaryMultiplier * m
            for (sw in q.secondaries) {
                rawMap[sw.traitId] = rawMap[sw.traitId]!! + secondaryMultiplier * sw.weight * m
            }
        }

        // 6) computeMaxPotential (yalnız cevaplananlar) + MAX SPLIT DIAGNOSTIC
        val maxBase: MutableMap<String, Float> = mutableMapOf()
        val primaryMax = mutableMapOf<String, Float>()
        val secondaryMax = mutableMapOf<String, Float>()

        for (q in questionsUsed) {
            maxBase[q.primaryTraitId] = (maxBase[q.primaryTraitId] ?: 0f) + primaryMultiplier
            primaryMax[q.primaryTraitId] = (primaryMax[q.primaryTraitId] ?: 0f) + primaryMultiplier
            for (sw in q.secondaries) {
                val add = secondaryMultiplier * sw.weight
                maxBase[sw.traitId] = (maxBase[sw.traitId] ?: 0f) + add
                secondaryMax[sw.traitId] = (secondaryMax[sw.traitId] ?: 0f) + add
            }
        }

        // MAX split log (primary | secondary | total)
        run {
            val sb = StringBuilder("MAX SPLIT (primary | secondary | total)\n")
            (rawMap.keys + maxBase.keys).distinct().sorted().forEach { t ->
                val p = primaryMax[t] ?: 0f
                val s = secondaryMax[t] ?: 0f
                val tot = maxBase[t] ?: 0f
                sb.append("$t : ${"%.3f".format(Locale.US, p)} | ${"%.3f".format(Locale.US, s)} | ${"%.3f".format(Locale.US, tot)}\n")
            }
            Log.i("ScoreCalculator", sb.toString().trim())
        }

        // 7) fair (0..80) + jitter (kapalı)
        val maxYes = maxBase.mapValues { it.value * yesFactor }
        data class FairTmp(val traitId: String, val raw: Float, val max: Float, val fairPercent: Float)
        val fairList = mutableListOf<FairTmp>()
        for ((trait, r) in rawMap) {
            val mYes = (maxYes[trait] ?: 0.0001f).coerceAtLeast(0.0001f)
            val rPos = r.coerceAtLeast(1f) // Unity ile aynı
            var fair = ((rPos / mYes).coerceIn(0f, 1f)) * 80f
            fairList.add(FairTmp(trait, r, mYes, fair))
        }

        // 8) softmax benzeri ~%20
        val eps = 0.05f
        val gamma = 0.75f
        val adjusted = FloatArray(fairList.size)
        var sumAdj = 0f
        for (i in fairList.indices) {
            val frac = (fairList[i].raw / fairList[i].max).coerceIn(0f, 1f)
            var v = (frac + eps).pow(gamma)
            adjusted[i] = v
            sumAdj += adjusted[i]
        }

        // 9) Sonuç + isim + sıralama
        val traitNameById = traits.associate { it.traitId.trim() to it.traitName }
        val finalList = fairList.mapIndexed { i, f ->
            val disp = (adjusted[i] / sumAdj) * 20f
            ScoreResult(
                traitId = f.traitId,
                traitName = traitNameById[f.traitId] ?: "Bilinmeyen Özellik",
                raw = f.raw,
                max = f.max,
                fairPercent = f.fairPercent,
                softmaxPercent = f.fairPercent+disp,
                displayPercent = f.fairPercent+disp,
                score = f.fairPercent+disp
            )
        }.sortedByDescending { it.displayPercent }

        // 10) DIAGNOSTIC: trait bazlı raw/max/fair log (debug)
        val dbg = buildString {
            append("TRAITS (raw | max | fair)\n")
            finalList.forEach { r -> append("${r.traitId} : ${"%.3f".format(Locale.US, r.raw)} | ${"%.3f".format(Locale.US, r.max)} | ${"%.1f".format(Locale.US, r.fairPercent)}\n") }
        }
        Log.d("ScoreCalculator", dbg.trim())

        // 11) UI log (istenen format)
        val logText = buildString {
            append("[Row ${finalList.size}]\n")
            finalList.forEachIndexed { idx, r ->
                append("${idx + 1}. ${r.traitName} | Rate%: ${"%.1f".format(Locale.US, r.displayPercent)}\n")
            }
        }
        Log.i("ScoreCalculator", logText.trim())

        return finalList
    }

    private fun randomInRange(range: ClosedFloatingPointRange<Float>): Float {
        return range.start + Math.random().toFloat() * (range.endInclusive - range.start)
    }
}
