package com.balikllama.xpguiderdemo.repository

import com.balikllama.xpguiderdemo.data.local.entity.AnswerType
import com.balikllama.xpguiderdemo.data.local.entity.QuestionEntity
import com.balikllama.xpguiderdemo.data.local.entity.SolvedQuestion

/**
 * Veri kaynakları (lokal veritabanı, uzak sunucu vb.) ile ViewModel arasındaki iletişimi yöneten arayüz.
 * ViewModel, verilere nasıl erişildiğinin detaylarını bilmez, sadece bu metotları çağırır.
 */
interface TestRepository {

    /**
     * Lokal veritabanındaki tüm soruları getirir.
     * @return Soru listesini döndürür.
     */
    suspend fun getQuestions(): List<QuestionEntity>

    /**
     * Bir soruya verilen cevabı veritabanına ekler veya günceller.
     * @param testSessionId Mevcut test seansının benzersiz kimliği.
     * @param questionId Cevaplanan sorunun kimliği.
     * @param answer Kullanıcının verdiği cevap (YES, NO, NEUTRAL).
     */
    suspend fun saveOrUpdateAnswer(testSessionId: String, questionId: String, answer: AnswerType)
    suspend fun getAnswerForQuestion(testSessionId: String, questionId: String): SolvedQuestion? // <-- DÜZELTME: String

    suspend fun getAllAnswersForSession(testSessionId: String): List<SolvedQuestion>
}

