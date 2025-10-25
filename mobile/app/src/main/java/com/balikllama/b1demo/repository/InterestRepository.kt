package com.balikllama.b1demo.repository

import androidx.compose.ui.geometry.isEmpty
import com.balikllama.b1demo.data.local.dao.InterestDao
import com.balikllama.b1demo.data.local.entity.InterestEntity
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
                InterestEntity(id = "I1", areaOfInterest = "Teknoloji"),
                InterestEntity(id = "I2", areaOfInterest = "Bilim"),
                InterestEntity(id = "I3", areaOfInterest = "Sanat"),
                InterestEntity(id = "I4", areaOfInterest = "Tarih")
            )
            interestDao.insertAll(initialList)
        }
    }
}
