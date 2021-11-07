package com.quizapp.data.remote.api.dto

import com.google.gson.annotations.SerializedName

data class QuestionResponseDto(
    @SerializedName("response_code")
    val responseCode: Int,
    @SerializedName("results")
    val questions: List<QuestionDto>
)