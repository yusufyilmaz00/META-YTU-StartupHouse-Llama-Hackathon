package com.balikllama.xpguiderdemo.repository


import com.balikllama.xpguiderdemo.data.local.dao.TestResultDao
import com.balikllama.xpguiderdemo.data.local.entity.TestResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestResultRepository @Inject constructor(private val testResultDao: TestResultDao) {

    /**
     * Veritabanındaki tüm test sonuçlarını getirir.
     */
    suspend fun getTestResults(): List<TestResult> {
        return testResultDao.getAllTestResults()
    }

    /**
     * Verilen test sonuçları listesini veritabanına kaydeder.
     * Kaydetmeden önce eski verileri temizler.
     * @param results Kaydedilecek yeni TestResult listesi.
     */
    suspend fun saveTestResults(results: List<TestResult>) {
        testResultDao.insertAll(results)
    }
}