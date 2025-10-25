package com.balikllama.xpguiderdemo.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.balikllama.xpguiderdemo.data.local.entity.SolvedQuestion


@Dao
interface SolvedQuestionDao {

    @Upsert
    suspend fun upsertSolvedQuestion(solvedQuestion: SolvedQuestion)
    @Query("SELECT * FROM solved_questions WHERE testSessionId = :testSessionId AND questionId = :questionId")
    suspend fun getAnswerForQuestion(testSessionId: String, questionId: String): SolvedQuestion?
    @Query("SELECT * FROM solved_questions WHERE testSessionId = :testSessionId")
    suspend fun getAllAnswersForSession(testSessionId: String): List<SolvedQuestion>
}
