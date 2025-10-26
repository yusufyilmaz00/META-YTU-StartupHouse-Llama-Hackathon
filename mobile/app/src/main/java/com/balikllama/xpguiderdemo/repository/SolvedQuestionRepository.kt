package com.balikllama.xpguiderdemo.repository

import com.balikllama.xpguiderdemo.data.local.dao.SolvedQuestionDao
import com.balikllama.xpguiderdemo.data.local.entity.SolvedQuestion
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SolvedQuestionRepository @Inject constructor(
    private val solvedQuestionDao: SolvedQuestionDao
) {
    fun getAllSolvedQuestions(): Flow<List<SolvedQuestion>> {
        return solvedQuestionDao.getAllSolvedQuestionsStream()
    }
}