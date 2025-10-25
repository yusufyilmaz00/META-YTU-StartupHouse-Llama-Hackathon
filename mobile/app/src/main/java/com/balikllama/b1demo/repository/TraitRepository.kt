package com.balikllama.b1demo.repository


import com.balikllama.b1demo.data.local.dao.TraitDao
import com.balikllama.b1demo.data.local.entity.TraitEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraitRepository @Inject constructor(
    private val traitDao: TraitDao
) {
    fun getAllTraits(): Flow<List<TraitEntity>> {
        return traitDao.getAllTraits()
    }

    suspend fun insertInitialTraits() {
        if (traitDao.getAllTraits().first().isEmpty()) { // Sadece DB boşsa ekle
            val initialList = listOf(
                TraitEntity(traitId = "A", traitName = "Analitik"),
                TraitEntity(traitId = "B", traitName = "Yaratıcı"),
                TraitEntity(traitId = "C", traitName = "Lider"),
                TraitEntity(traitId = "D", traitName = "Empatik")
            )
            traitDao.insertAll(initialList)
        }
    }
}
