package com.balikllama.b1demo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.balikllama.b1demo.data.local.entity.TraitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TraitDao {

    @Query("SELECT * FROM trait_list")
    fun getAllTraits(): Flow<List<TraitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(traits: List<TraitEntity>)

    @Query("DELETE FROM trait_list")
    suspend fun clearAll()
}
