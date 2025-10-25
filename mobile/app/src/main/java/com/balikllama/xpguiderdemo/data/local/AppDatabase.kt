package com.balikllama.xpguiderdemo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.balikllama.xpguiderdemo.data.local.dao.CalculationFactorDao
import com.balikllama.xpguiderdemo.data.local.dao.InterestDao
import com.balikllama.xpguiderdemo.data.local.dao.QuestionDao
import com.balikllama.xpguiderdemo.data.local.entity.InterestEntity
import com.balikllama.xpguiderdemo.data.local.dao.TraitDao
import com.balikllama.xpguiderdemo.data.local.entity.CalculationFactorEntity
import com.balikllama.xpguiderdemo.data.local.entity.QuestionEntity
import com.balikllama.xpguiderdemo.data.local.entity.TraitEntity

@Database(
    entities = [
        InterestEntity::class,
        TraitEntity::class,
        QuestionEntity::class,
        CalculationFactorEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun interestDao(): InterestDao
    abstract fun traitDao(): TraitDao
    abstract fun questionDao(): QuestionDao
    abstract fun calculationFactorDao(): CalculationFactorDao
}
