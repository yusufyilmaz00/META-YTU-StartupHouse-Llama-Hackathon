package com.balikllama.xpguiderdemo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.balikllama.xpguiderdemo.data.local.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {

    @Query("SELECT * FROM question_list")
    fun getAllQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM question_list WHERE active = 1") // Sadece aktif olanları getir
    fun getActiveQuestions(): Flow<List<QuestionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<QuestionEntity>)

    @Query("DELETE FROM question_list")
    suspend fun clearAll()
}
