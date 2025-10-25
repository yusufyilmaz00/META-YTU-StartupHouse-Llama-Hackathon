package com.balikllama.b1demo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.balikllama.b1demo.data.local.entity.CalculationFactorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CalculationFactorDao {

    @Query("SELECT * FROM calculation_factors")
    fun getAllFactors(): Flow<List<CalculationFactorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(factors: List<CalculationFactorEntity>)

    @Query("DELETE FROM calculation_factors")
    suspend fun clearAll()
}
