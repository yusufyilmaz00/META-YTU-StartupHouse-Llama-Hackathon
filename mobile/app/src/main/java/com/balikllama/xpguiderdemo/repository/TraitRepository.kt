package com.balikllama.xpguiderdemo.repository


import com.balikllama.xpguiderdemo.data.local.dao.TraitDao
import com.balikllama.xpguiderdemo.data.local.entity.TraitEntity
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


    suspend fun clearAllTraits() {
        traitDao.clearAll()
    }

    suspend fun insertInitialTraits() {
        if (traitDao.getAllTraits().first().isEmpty()) { // Sadece DB boşsa ekle
            val initialList = listOf(
                TraitEntity(traitId = "A", traitName = "Analitik Zeka"),
                TraitEntity(traitId = "B", traitName = "Yaratıcı Zeka"),
                TraitEntity(traitId = "C", traitName = "Duygusal Zeka"),
                TraitEntity(traitId = "D", traitName = "Sosyal Zeka"),
                TraitEntity(traitId = "E", traitName = "Teknik Zeka"),
                TraitEntity(traitId = "F", traitName = "Estetik Zeka"),
                TraitEntity(traitId = "G", traitName = "Pratik Zeka"),
                TraitEntity(traitId = "H", traitName = "Doğal Zeka"),
                TraitEntity(traitId = "I", traitName = "Bilimsel Zeka"),
                TraitEntity(traitId = "J", traitName = "Fiziksel Zeka"),
                TraitEntity(traitId = "K", traitName = "Ruhsal Zeka"),
                TraitEntity(traitId = "L", traitName = "Stratejik Zeka"),
                TraitEntity(traitId = "M", traitName = "Girişimcilik Zekası"),
                TraitEntity(traitId = "N", traitName = "Dilsel Zeka"),
                TraitEntity(traitId = "O", traitName = "Dijital Zeka"),
                TraitEntity(traitId = "P", traitName = "İletişimsel Zeka")
                )
            traitDao.insertAll(initialList)
        }
    }
}
