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
                TraitEntity(traitId = "A", traitName = "Analytical Intelligence"),
                TraitEntity(traitId = "B", traitName = "Creative Intelligence"),
                TraitEntity(traitId = "C", traitName = "Emotional Intelligence"),
                TraitEntity(traitId = "D", traitName = "Social Intelligence"),
                TraitEntity(traitId = "E", traitName = "Technical Intelligence"),
                TraitEntity(traitId = "F", traitName = "Aesthetic Intelligence"),
                TraitEntity(traitId = "G", traitName = "Practical Intelligence"),
                TraitEntity(traitId = "H", traitName = "Naturalistic Intelligence"),
                TraitEntity(traitId = "I", traitName = "Scientific Intelligence"),
                TraitEntity(traitId = "J", traitName = "Physical Intelligence"),
                TraitEntity(traitId = "K", traitName = "Spiritual Intelligence"),
                TraitEntity(traitId = "L", traitName = "Strategic Intelligence"),
                TraitEntity(traitId = "M", traitName = "Entrepreneurial Intelligence"),
                TraitEntity(traitId = "N", traitName = "Linguistic Intelligence"),
                TraitEntity(traitId = "O", traitName = "Digital Intelligence"),
                TraitEntity(traitId = "P", traitName = "Communicative Intelligence")
            )
            traitDao.insertAll(initialList)
        }
    }

}
