package com.balikllama.xpguiderdemo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.balikllama.xpguiderdemo.data.local.entity.InterestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InterestDao {

    @Query("SELECT * FROM interest_list")
    fun getAllInterests(): Flow<List<InterestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(interests: List<InterestEntity>)

    @Query("DELETE FROM interest_list")
    suspend fun clearAll()
}
