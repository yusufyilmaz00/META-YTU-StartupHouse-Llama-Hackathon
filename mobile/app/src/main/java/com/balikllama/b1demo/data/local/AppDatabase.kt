package com.balikllama.b1demo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.balikllama.b1demo.data.local.dao.InterestDao
import com.balikllama.b1demo.data.local.entity.InterestEntity

@Database(
    entities = [InterestEntity::class], // Veritabanında olacak tablolar
    version = 1,                         // Veritabanı şeması değiştiğinde artırılmalı
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun interestDao(): InterestDao
}
