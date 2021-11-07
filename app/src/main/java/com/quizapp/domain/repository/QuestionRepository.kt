package com.quizapp.domain.repository

import com.quizapp.data.remote.api.dto.QuestionResponseDto

interface QuestionRepository {

    suspend fun getQuestions() : QuestionResponseDto

    suspend fun getSpecificQuestions(category: String, difficulty: String = "") : QuestionResponseDto
}