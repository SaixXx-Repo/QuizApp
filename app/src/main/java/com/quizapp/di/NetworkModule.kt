package com.quizapp.di

import com.quizapp.common.Constants.BASE_URL
import com.quizapp.data.remote.api.QuizApi
import com.quizapp.data.repository.QuestionRepositoryImpl
import com.quizapp.domain.repository.QuestionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule{

    @Provides
    @Singleton
    fun provideQuizApi(): QuizApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuizApi::class.java)
    }

    @Provides
    @Singleton
    fun provideQuestionRepository(api: QuizApi): QuestionRepository{
        return QuestionRepositoryImpl(api)
    }

}