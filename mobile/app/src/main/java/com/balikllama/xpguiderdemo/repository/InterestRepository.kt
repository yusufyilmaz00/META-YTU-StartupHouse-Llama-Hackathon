package com.balikllama.xpguiderdemo.repository

import com.balikllama.xpguiderdemo.data.local.dao.InterestDao
import com.balikllama.xpguiderdemo.data.local.entity.InterestEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterestRepository @Inject constructor(
    private val interestDao: InterestDao // Hilt, bu DAO'yu DatabaseModule'den sağlayacak
) {

    /**
     * Veritabanındaki tüm ilgi alanlarını bir Flow olarak döndürür.
     * UI bu akışı dinleyerek değişiklikleri anında yansıtabilir.
     */
    fun getAllInterests(): Flow<List<InterestEntity>> {
        return interestDao.getAllInterests()
    }

    suspend fun clearAllInterests() {
        interestDao.clearAll()
    }

    /**
     * Uzak sunucudan gelen ilgi alanlarını lokal veritabanına kaydeder.
     * Önce mevcut verileri temizleyip sonra yenilerini ekler (Sync mantığı).
     */
    suspend fun refreshInterests(interests: List<InterestEntity>) {
        interestDao.clearAll() // Önce eski verileri temizle
        interestDao.insertAll(interests) // Sonra yenilerini ekle
    }

    /**
     * Sadece test veya başlangıç verisi eklemek için kullanılabilir bir fonksiyon.
     */
    suspend fun insertInitialInterests() {
        if (interestDao.getAllInterests().first().isEmpty()) { // Sadece DB boşsa ekle
            val initialList = listOf(
                InterestEntity(id = "I1", areaOfInterest = "Creativity"),
                InterestEntity(id = "I2", areaOfInterest = "Manual skills"),
                InterestEntity(id = "I3", areaOfInterest = "Numerical thinking"),
                InterestEntity(id = "I4", areaOfInterest = "Analytical mind"),
                InterestEntity(id = "I5", areaOfInterest = "Problem solving"),
                InterestEntity(id = "I6", areaOfInterest = "Detail focus"),
                InterestEntity(id = "I7", areaOfInterest = "Teamwork"),
                InterestEntity(id = "I8", areaOfInterest = "Communication"),
                InterestEntity(id = "I9", areaOfInterest = "Leadership"),
                InterestEntity(id = "I10", areaOfInterest = "Planning"),
                InterestEntity(id = "I11", areaOfInterest = "Empathy"),
                InterestEntity(id = "I12", areaOfInterest = "Patience"),
                InterestEntity(id = "I13", areaOfInterest = "Decision making"),
                InterestEntity(id = "I14", areaOfInterest = "Time management"),
                InterestEntity(id = "I15", areaOfInterest = "Innovation"),
                InterestEntity(id = "I16", areaOfInterest = "Responsibility"),
                InterestEntity(id = "I17", areaOfInterest = "Practical thinking"),
                InterestEntity(id = "I18", areaOfInterest = "Logical reasoning"),
                InterestEntity(id = "I19", areaOfInterest = "Aesthetic sense"),
                InterestEntity(id = "I20", areaOfInterest = "Mechanical mind"),
                InterestEntity(id = "I21", areaOfInterest = "Technical curiosity"),
                InterestEntity(id = "I22", areaOfInterest = "Love of nature"),
                InterestEntity(id = "I23", areaOfInterest = "Social skills"),
                InterestEntity(id = "I24", areaOfInterest = "Discipline"),
                InterestEntity(id = "I25", areaOfInterest = "Teaching desire"),
                InterestEntity(id = "I26", areaOfInterest = "Persuasion"),
                InterestEntity(id = "I27", areaOfInterest = "Craft interest"),
                InterestEntity(id = "I28", areaOfInterest = "Color harmony"),
                InterestEntity(id = "I29", areaOfInterest = "Emotional awareness"),
                InterestEntity(id = "I30", areaOfInterest = "Research interest"),
                InterestEntity(id = "I31", areaOfInterest = "Productivity"),
                InterestEntity(id = "I32", areaOfInterest = "Entrepreneurship"),
                InterestEntity(id = "I33", areaOfInterest = "Strategic thinking"),
                InterestEntity(id = "I34", areaOfInterest = "Visual memory"),
                InterestEntity(id = "I35", areaOfInterest = "Tech interest"),
                InterestEntity(id = "I36", areaOfInterest = "Resilience"),
                InterestEntity(id = "I37", areaOfInterest = "Helpfulness"),
                InterestEntity(id = "I38", areaOfInterest = "Flexibility"),
                InterestEntity(id = "I39", areaOfInterest = "Curiosity"),
                InterestEntity(id = "I40", areaOfInterest = "Systematic work"),
                InterestEntity(id = "I41", areaOfInterest = "Experimental mind"),
                InterestEntity(id = "I42", areaOfInterest = "Customer focus"),
                InterestEntity(id = "I43", areaOfInterest = "Technical drawing"),
                InterestEntity(id = "I44", areaOfInterest = "Design sense"),
                InterestEntity(id = "I45", areaOfInterest = "Safety awareness")
            )
            interestDao.insertAll(initialList)
        }
    }
}