package com.balikllama.b1demo.repository

import com.balikllama.b1demo.data.local.dao.CalculationFactorDao
import com.balikllama.b1demo.data.local.entity.CalculationFactorEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalculationFactorRepository @Inject constructor(
    private val factorDao: CalculationFactorDao
) {
    fun getAllFactors(): Flow<List<CalculationFactorEntity>> {
        return factorDao.getAllFactors()
    }

    suspend fun insertInitialFactors() {
        if (factorDao.getAllFactors().first().isEmpty()) {
            val initialList = listOf(
                CalculationFactorEntity(key = "answer_yes", value = 1.0f),
                CalculationFactorEntity(key = "answer_no", value = -1.0f),
                CalculationFactorEntity(key = "answer_neutral", value = 0.0f),
                CalculationFactorEntity(key = "base_multiplier", value = 1.2f)
            )
            factorDao.insertAll(initialList)
        }
    }
}
