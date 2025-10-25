package com.balikllama.xpguiderdemo.repository

import com.balikllama.xpguiderdemo.data.local.dao.QuestionDao
import com.balikllama.xpguiderdemo.data.local.dao.SolvedQuestionDao
import com.balikllama.xpguiderdemo.data.local.entity.AnswerType
import com.balikllama.xpguiderdemo.data.local.entity.QuestionEntity
import com.balikllama.xpguiderdemo.data.local.entity.SolvedQuestion
import com.balikllama.xpguiderdemo.repository.TestRepository
import kotlinx.coroutines.flow.first
import java.util.Date
import javax.inject.Inject

/**
 * TestRepository arayüzünün somut uygulaması.
 * Veritabanı işlemlerini gerçekleştirmek için DAO'ları kullanır.
 * @Inject constructor: Hilt'in bu sınıfın nasıl oluşturulacağını bilmesini sağlar.
 */
class TestRepositoryImpl @Inject constructor(
    private val questionDao: QuestionDao,
    private val solvedQuestionDao: SolvedQuestionDao
) : TestRepository {

    override suspend fun getQuestions(): List<QuestionEntity> {
        // QuestionDao'nun getActiveQuestions() metodu Flow döndürüyor.
        // Bunu tek seferlik bir liste almak için .first() ile kullanmalıyız.
        return questionDao.getActiveQuestions().first()
    }

    override suspend fun saveOrUpdateAnswer(testSessionId: String, questionId: String, answer: AnswerType) {
        val existingAnswer = getAnswerForQuestion(testSessionId, questionId)

        val solvedQuestion = SolvedQuestion(
            id = existingAnswer?.id ?: 0,
            testSessionId = testSessionId,
            questionId = questionId,
            answer = answer,
            solvedAt = Date()
        )
        solvedQuestionDao.upsertSolvedQuestion(solvedQuestion)
    }

    override suspend fun getAnswerForQuestion(testSessionId: String, questionId: String): SolvedQuestion? {
        return solvedQuestionDao.getAnswerForQuestion(testSessionId, questionId)
    }

    override suspend fun getAllAnswersForSession(testSessionId: String): List<SolvedQuestion> {
        return solvedQuestionDao.getAllAnswersForSession(testSessionId)
    }
}
