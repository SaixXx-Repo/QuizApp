package com.quizapp.data.remote.api.dto

import com.google.gson.annotations.SerializedName
import com.quizapp.domain.model.Question

data class QuestionDto(
    val category: String,
    @SerializedName("correct_answer")
    val correctAnswer: String,
    val difficulty: String,
    @SerializedName("incorrect_answers")
    val incorrectAnswers: List<String>,
    val question: String,
    val type: String
)


fun QuestionDto.toQuestion(): Question {
    return Question(
        correctAnswer= correctAnswer,
        incorrectAnswers= incorrectAnswers,
        question= question
    )
}