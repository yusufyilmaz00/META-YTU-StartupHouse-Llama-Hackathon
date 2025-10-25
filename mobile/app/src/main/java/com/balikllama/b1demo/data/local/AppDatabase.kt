package com.balikllama.b1demo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.balikllama.b1demo.data.local.dao.InterestDao
import com.balikllama.b1demo.data.local.entity.InterestEntity
import com.balikllama.b1demo.data.local.dao.TraitDao
import com.balikllama.b1demo.data.local.entity.TraitEntity

@Database(
    entities = [
        InterestEntity::class,
        TraitEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun interestDao(): InterestDao
    abstract fun traitDao(): TraitDao
}
