package com.balikllama.xpguiderdemo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.balikllama.xpguiderdemo.data.local.dao.CalculationFactorDao
import com.balikllama.xpguiderdemo.data.local.dao.InterestDao
import com.balikllama.xpguiderdemo.data.local.dao.QuestionDao
import com.balikllama.xpguiderdemo.data.local.dao.SolvedQuestionDao
import com.balikllama.xpguiderdemo.data.local.dao.TestResultDao
import com.balikllama.xpguiderdemo.data.local.entity.InterestEntity
import com.balikllama.xpguiderdemo.data.local.dao.TraitDao
import com.balikllama.xpguiderdemo.data.local.entity.CalculationFactorEntity
import com.balikllama.xpguiderdemo.data.local.entity.Converters
import com.balikllama.xpguiderdemo.data.local.entity.QuestionEntity
import com.balikllama.xpguiderdemo.data.local.entity.SolvedQuestion
import com.balikllama.xpguiderdemo.data.local.entity.TestResult
import com.balikllama.xpguiderdemo.data.local.entity.TraitEntity

@Database(
    entities = [
        InterestEntity::class,
        TraitEntity::class,
        QuestionEntity::class,
        CalculationFactorEntity::class,
        SolvedQuestion::class,
        TestResult::class
    ],
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun interestDao(): InterestDao
    abstract fun traitDao(): TraitDao
    abstract fun questionDao(): QuestionDao
    abstract fun calculationFactorDao(): CalculationFactorDao
    abstract fun solvedQuestionDao(): SolvedQuestionDao
    abstract fun testResultDao(): TestResultDao
}
