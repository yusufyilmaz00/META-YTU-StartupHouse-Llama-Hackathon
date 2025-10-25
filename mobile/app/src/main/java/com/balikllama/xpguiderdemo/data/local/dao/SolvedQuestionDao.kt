package com.balikllama.xpguiderdemo.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.balikllama.xpguiderdemo.data.local.entity.SolvedQuestion
import kotlinx.coroutines.flow.Flow


@Dao
interface SolvedQuestionDao {

    @Upsert
    suspend fun upsertSolvedQuestion(solvedQuestion: SolvedQuestion)
    @Query("SELECT * FROM solved_questions WHERE testSessionId = :testSessionId AND questionId = :questionId")
    suspend fun getAnswerForQuestion(testSessionId: String, questionId: String): SolvedQuestion?
    @Query("SELECT * FROM solved_questions WHERE testSessionId = :testSessionId")
    suspend fun getAllAnswersForSession(testSessionId: String): List<SolvedQuestion>
    @Query("SELECT * FROM solved_questions ORDER BY testSessionId DESC")
    fun getAllSolvedQuestionsStream(): Flow<List<SolvedQuestion>>

}
