package com.quizapp.data.repository

import com.quizapp.data.remote.api.QuizApi
import com.quizapp.data.remote.api.dto.QuestionResponseDto
import com.quizapp.domain.repository.QuestionRepository
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val api: QuizApi
) : QuestionRepository{

    override suspend fun getQuestions(): QuestionResponseDto {
        return api.getQuestions()
    }

    override suspend fun getSpecificQuestions(category: String, difficulty: String): QuestionResponseDto {
        return api.getSpecificQuestions(category, difficulty)
    }
}