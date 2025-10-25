package com.balikllama.b1demo.repository

import com.balikllama.b1demo.data.local.dao.QuestionDao
import com.balikllama.b1demo.data.local.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionRepository @Inject constructor(
    private val questionDao: QuestionDao
) {
    fun getAllQuestions(): Flow<List<QuestionEntity>> {
        return questionDao.getAllQuestions()
    }

    suspend fun insertInitialQuestions() {
        // Bu tablonun boş olup olmadığını kontrol et
        if (questionDao.getAllQuestions().first().isEmpty()) {
            val initialList = listOf(
                QuestionEntity("Q1", "Bir grup projesinde liderlik rolünü üstlenir misin?", "C", "A", 0.3f, "D", 0.1f, "B", 0.0f, true),
                QuestionEntity("Q2", "Yeni bir teknolojik aleti kurcalamaktan keyif alır mısın?", "A", "B", 0.2f, "C", 0.1f, "D", 0.0f, true),
                QuestionEntity("Q3", "Bir arkadaşın sorununu anlatırken onu anladığını hissettirir misin?", "D", "B", 0.2f, "A", 0.1f, "C", 0.0f, false),
                QuestionEntity("Q4", "Boş zamanlarında sanatsal bir aktiviteyle uğraşır mısın?", "B", "D", 0.4f, "A", 0.1f, "C", 0.0f, true)
            )
            questionDao.insertAll(initialList)
        }
    }
}
