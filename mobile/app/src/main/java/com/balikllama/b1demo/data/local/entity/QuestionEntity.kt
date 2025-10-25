package com.balikllama.b1demo.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "question_list",
    foreignKeys = [
        ForeignKey(
            entity = TraitEntity::class,
            parentColumns = ["trait_id"],
            childColumns = ["primary_id"],
            onDelete = ForeignKey.CASCADE
        )
        // Not: Diğer s1_id, s2_id, s3_id için de foreign key eklenebilir,
        // ama şimdilik ana ID için eklemek yeterli.
    ]
)
data class QuestionEntity(
    @PrimaryKey
    @ColumnInfo(name = "q_id")
    val qId: String,

    @ColumnInfo(name = "q_text")
    val qText: String,

    @ColumnInfo(name = "primary_id", index = true) // Hızlı arama için index
    val primaryId: String,

    @ColumnInfo(name = "s1_id")
    val s1Id: String,

    @ColumnInfo(name = "s1_w")
    val s1w: Float, // Ondalık değerler için Float veya Double

    @ColumnInfo(name = "s2_id")
    val s2Id: String,

    @ColumnInfo(name = "s2_w")
    val s2w: Float,

    @ColumnInfo(name = "s3_id")
    val s3Id: String,

    @ColumnInfo(name = "s3_w")
    val s3w: Float,

    @ColumnInfo(name = "active")
    val active: Boolean
)
