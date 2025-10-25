package com.balikllama.xpguiderdemo.repository

import com.balikllama.xpguiderdemo.data.local.dao.CalculationFactorDao
import com.balikllama.xpguiderdemo.data.local.entity.CalculationFactorEntity
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


    suspend fun clearAllFactors() {
        factorDao.clearAll()
    }
    suspend fun insertInitialFactors() {
        if (factorDao.getAllFactors().first().isEmpty()) {
            val initialList = listOf(
                CalculationFactorEntity(key = "answer_yes", value = 1.0f),
                CalculationFactorEntity(key = "answer_maybe", value = 0.2f),
                CalculationFactorEntity(key = "answer_no", value = -0.4f),
                CalculationFactorEntity(key = "weight_primary", value = 2.0f),
                CalculationFactorEntity(key = "weight_secondary", value = 0.1f),
                )
            factorDao.insertAll(initialList)
        }
    }
}
