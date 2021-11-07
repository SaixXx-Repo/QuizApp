package com.quizapp.domain.model

import com.google.gson.annotations.SerializedName

data class Question(
    val correctAnswer: String,
    val incorrectAnswers: List<String>,
    val question: String
)
