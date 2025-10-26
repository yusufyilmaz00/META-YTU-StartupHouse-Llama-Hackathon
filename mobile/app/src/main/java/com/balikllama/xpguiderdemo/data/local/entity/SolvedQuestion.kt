package com.balikllama.xpguiderdemo.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Date

/**
 * Represents the possible answers a user can give.
 * Using an enum ensures type safety and data integrity.
 */
enum class AnswerType {
    YES,
    NO,
    NEUTRAL
}

/**
 * Type converters to allow Room to store complex types like Date and our custom Enum.
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromAnswerType(value: String?): AnswerType? {
        return value?.let { AnswerType.valueOf(it) }
    }

    @TypeConverter
    fun answerTypeToString(answerType: AnswerType?): String? {
        return answerType?.name
    }
}


/**
 * Represents a single answered question in a test session.
 * It links a specific question to a user's answer at a specific time.
 */
@Entity(
    tableName = "solved_questions",
    foreignKeys = [
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["q_id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["questionId"]), Index(value = ["testSessionId"])]
)
@TypeConverters(Converters::class)
data class SolvedQuestion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val testSessionId: String,
    val questionId: String,
    val answer: AnswerType,
    val solvedAt: Date
)
