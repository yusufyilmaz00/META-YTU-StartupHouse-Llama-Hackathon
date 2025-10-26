package com.balikllama.xpguiderdemo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.balikllama.xpguiderdemo.data.local.entity.TestResult

@Dao
interface TestResultDao {

    /**
     * Test sonuçlarını veritabanına ekler. Eğer aynı isimde bir sonuç varsa,
     * eskisini yenisiyle değiştirir.
     * @param results Eklenecek TestResult listesi.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(results: List<TestResult>)

    /**
     * Veritabanındaki tüm test sonuçlarını getirir.
     * @return TestResult listesi.
     */
    @Query("SELECT * FROM test_results")
    suspend fun getAllTestResults(): List<TestResult>

    /**
     * Tüm test sonuçları tablosunu temizler.
     * Yeni bir test yapıldığında eski sonuçları silmek için kullanılabilir.
     */
    @Query("DELETE FROM test_results")
    suspend fun clearAll()
}