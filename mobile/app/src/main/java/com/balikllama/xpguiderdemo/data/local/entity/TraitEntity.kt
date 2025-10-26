package com.balikllama.xpguiderdemo.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trait_list")
data class TraitEntity(
    @PrimaryKey
    @ColumnInfo(name = "trait_id")
    val traitId: String, // "A", "B", "C" gibi

    @ColumnInfo(name = "trait_name")
    val traitName: String
)
