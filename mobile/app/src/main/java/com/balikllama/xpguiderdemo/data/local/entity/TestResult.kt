package com.balikllama.xpguiderdemo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Kişilik testi analiz sonuçlarını temsil eden veritabanı tablosu.
 *
 * @param id Otomatik artan birincil anahtar.
 * @param name Zeka özelliğinin adı (örn: "analytical", "emotional").
 * @param percentage Bu özelliğin yüzdelik skoru.
 */
@Entity(tableName = "test_results")
data class TestResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val percentage: Float
)