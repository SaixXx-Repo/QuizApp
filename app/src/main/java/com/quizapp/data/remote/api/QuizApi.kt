package com.quizapp.data.remote.api

import com.quizapp.data.remote.api.dto.QuestionResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface QuizApi {

    @GET("/api.php?amount=10&type=multiple")
    suspend fun getQuestions(): QuestionResponseDto

    @GET("/api.php?amount=10&type=multiple")
    suspend fun getSpecificQuestions(
        @Query("category") category: String,
        @Query("difficulty") difficulty: String
    ): QuestionResponseDto

}