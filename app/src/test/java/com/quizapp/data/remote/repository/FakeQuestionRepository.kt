package com.quizapp.data.remote.repository

import com.quizapp.data.remote.api.dto.QuestionDto
import com.quizapp.data.remote.api.dto.QuestionResponseDto
import com.quizapp.domain.repository.QuestionRepository
import java.io.IOException

class FakeQuestionRepository: QuestionRepository {

    var emptyRepsonse = false
    var ioException = false

    private val emptyQuestion = QuestionResponseDto(1, emptyList())
    private val responseDto = QuestionResponseDto(
        0,
        listOf(QuestionDto(
            category = "Film",
            correctAnswer = "8",
            difficulty= "medium",
            incorrectAnswers = listOf(
                "7",
                "9",
                "5"
            ),
            question = "How many Harry Potter movies are there?",
            type = "multiple"
        ))
    )

    override suspend fun getQuestions(): QuestionResponseDto {
        return when (ioException){
            true -> throw IOException()
            false -> if(emptyRepsonse) emptyQuestion else responseDto

        }
    }

    override suspend fun getSpecificQuestions(
        category: String,
        difficulty: String
    ): QuestionResponseDto {
        return when (ioException){
            true -> throw IOException()
            false -> if(emptyRepsonse) emptyQuestion else responseDto

        }
    }
}