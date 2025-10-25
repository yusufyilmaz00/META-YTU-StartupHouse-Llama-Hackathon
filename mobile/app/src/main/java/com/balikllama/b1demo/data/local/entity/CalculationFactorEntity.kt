package com.balikllama.b1demo.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculation_factors")
data class CalculationFactorEntity(
    @PrimaryKey
    @ColumnInfo(name = "key")
    val key: String, // "answer_yes", "answer_no" gibi

    @ColumnInfo(name = "value")
    val value: Float // Pozitif veya negatif ondalık değer
)
