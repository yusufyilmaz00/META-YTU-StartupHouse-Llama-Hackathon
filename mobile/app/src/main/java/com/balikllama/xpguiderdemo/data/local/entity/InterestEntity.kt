package com.balikllama.xpguiderdemo.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "interest_list")
data class InterestEntity(
    @PrimaryKey    @ColumnInfo(name = "id")
    val id: String, // "I1", "I2" gibi değerler için String

    @ColumnInfo(name = "area_of_interest")
    val areaOfInterest: String
)