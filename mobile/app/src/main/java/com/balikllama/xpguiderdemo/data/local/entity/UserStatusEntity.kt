package com.balikllama.xpguiderdemo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_status")
data class UserStatusEntity(
    @PrimaryKey
    val email: String,
    val isSetupCompleted: Boolean
)
