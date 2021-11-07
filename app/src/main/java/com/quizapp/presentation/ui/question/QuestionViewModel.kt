package com.quizapp.presentation.ui.question

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizapp.common.Resource
import com.quizapp.domain.model.Question
import com.quizapp.domain.use_case.GetQuestionsUseCase
import com.quizapp.domain.use_case.GetSpecificQuestionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val getSpecificQuestionsUseCase: GetSpecificQuestionsUseCase
) : ViewModel() {

    val TAG = "QuestionViewModel"

    private val _questions : MutableLiveData<Resource<List<Question>>> = MutableLiveData()
    val questions : LiveData<Resource<List<Question>>> =  _questions

    fun getQuestions() = getQuestionsUseCase().flowOn(Dispatchers.IO).onEach{ result ->
        _questions.value = result
        Log.i(TAG,"Questions: Received -> ${result.data}")
    }.launchIn(viewModelScope)

    fun getSpecificQuestions(category: String, difficulty: String) = getSpecificQuestionsUseCase(category,difficulty).flowOn(Dispatchers.IO).onEach{ result ->
        _questions.value = result
        Log.i(TAG,"Questions: Received -> ${result.data}")
    }.launchIn(viewModelScope)

}