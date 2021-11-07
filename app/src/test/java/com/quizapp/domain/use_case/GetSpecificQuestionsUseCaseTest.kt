package com.quizapp.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.quizapp.common.Constants
import com.quizapp.data.remote.api.QuizApi
import com.quizapp.data.remote.repository.FakeQuestionRepository
import com.quizapp.data.repository.QuestionRepositoryImpl
import com.quizapp.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetSpecificQuestionsUseCaseTest {

    private lateinit var getSpecificQuestionsUseCase: GetSpecificQuestionsUseCase
    private lateinit var getSpecificQuestionsUseCaseFakeRepo: GetSpecificQuestionsUseCase
    private lateinit var fakeRepository: FakeQuestionRepository
    private lateinit var realRepository: QuestionRepository
    private lateinit var api: QuizApi

    @Before
    fun setUp() {

        api = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuizApi::class.java)

        realRepository = QuestionRepositoryImpl(api)
        fakeRepository = FakeQuestionRepository()

        getSpecificQuestionsUseCase = GetSpecificQuestionsUseCase(realRepository)
        getSpecificQuestionsUseCaseFakeRepo = GetSpecificQuestionsUseCase(fakeRepository)

    }


    @Test
    fun `Get Specific Questions, return empty list of Questions`() = runBlocking {
        fakeRepository.emptyRepsonse = true
        val result = getSpecificQuestionsUseCaseFakeRepo("dummy","dummy")

        assertThat(result.first().data).isNull()
        assertThat(result.first().message).isNull()

        assertThat(result.last().data).isEmpty()
        assertThat(result.last().message).isNull()
    }

    @Test
    fun `Get Hub by location Success, return hubs from copenhagen`() = runBlocking {

        val result = getSpecificQuestionsUseCase("10","medium")
        println(result.last().data)

        assertThat(result.first().data).isNull()
        assertThat(result.first().message).isNull()

        assertThat(result.last().data).isNotEmpty()
        assertThat(result.last().message).isNull()
    }

    @Test
    fun `Get Specific Questions with wrong parameters, return empty response`() = runBlocking {

        val result = getSpecificQuestionsUseCase("dummy","dummy")
        println(result.last().data)

        assertThat(result.first().data).isNull()
        assertThat(result.first().message).isNull()

        assertThat(result.last().data).isEmpty()
        assertThat(result.last().message).isNull()
    }

    @Test
    fun `Get Hub by location Error, IOException`() = runBlocking {

        fakeRepository.ioException = true
        val result = getSpecificQuestionsUseCaseFakeRepo("dummy","dummy")

        assertThat(result.first().data).isNull()
        assertThat(result.first().message).isNull()

        assertThat(result.last().data).isNull()
        assertThat(result.last().message).isEqualTo("No internet connection")
    }
}