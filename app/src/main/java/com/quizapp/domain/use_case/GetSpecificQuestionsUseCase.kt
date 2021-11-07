package com.quizapp.domain.use_case

import com.quizapp.common.Resource
import com.quizapp.data.remote.api.dto.toQuestion
import com.quizapp.domain.model.Question
import com.quizapp.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetSpecificQuestionsUseCase @Inject constructor(
    private val repository: QuestionRepository
) {

    operator fun invoke(category: String, difficulty: String) : Flow<Resource<List<Question>>> = flow {
        try {
            emit(Resource.Loading())
            val questions = repository.getSpecificQuestions(category,difficulty).questions.map { it.toQuestion() }
            emit(Resource.Success(questions))
        }catch (e: HttpException){ // Error Response from API
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        }catch (e: IOException){ // No connection to remote API, e.g. no Internet connection
            emit(Resource.Error("No internet connection"))
        }
    }
}