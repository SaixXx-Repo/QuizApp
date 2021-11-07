package com.quizapp.presentation.ui.question

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.quizapp.R
import com.quizapp.common.Constants.ANSWER_TIME
import com.quizapp.common.Constants.EXTRA_TIME
import com.quizapp.common.Resource
import com.quizapp.databinding.FragmentQuestionBinding
import com.quizapp.domain.model.Question
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class QuestionFragment : Fragment() {

    val TAG = "QuestionFragment"

    private val viewModel: QuestionViewModel by viewModels()

    private var _binding : FragmentQuestionBinding? = null
    private val binding get() = _binding!!

    private var questions: List<Question>? = null
    private var answer1: String = ""
    private var answer2: String = ""
    private var answer3: String = ""
    private var answer4: String = ""
    private var correctAnswer: String = ""
    private var currentQuestion = 0
    private var totalScore = 0
    private var usedTime = 0
    private var usedExtraTime = 0
    private var extraTime = 0L
    private var extraTimerIsRunning = false
    private lateinit var extraTimer : CountDownTimer
    private val answerTimes = ArrayList<Int>()

    val timer = object: CountDownTimer(ANSWER_TIME, 1) {

        override fun onTick(millisUntilFinished: Long) {
            binding.pbTime.progress = (millisUntilFinished.toInt() * 100 / ANSWER_TIME.toInt())
            usedTime = ANSWER_TIME.toInt() - millisUntilFinished.toInt()
        }

        override fun onFinish() {
            answerTimes.add(ANSWER_TIME.toInt())
            showCorrectAnswer(true)
            finishQuestion()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)

        val args = arguments
        val category = args?.getString("category")
        val difficulty = args?.getString("difficulty")

        if (category == "0"){
            viewModel.getQuestions()
        }
        else if(category != null && difficulty != null){
            viewModel.getSpecificQuestions(category,difficulty)
        }

        viewModel.questions.observe(viewLifecycleOwner, Observer { result ->
            when(result) {
                is Resource.Success -> {
                    questions = result.data
                    updateQuestion()
                    setViewsVisible()
                    setClickListeners()
                    timer.start()
                    Log.i(TAG,"Questions Received Successfully: ${result.data}")
                }
                is Resource.Error -> {
                    Log.e(TAG,result.message ?: "An unexpected error occured")
                }
                is Resource.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                }
            }
        })

        binding.btnNext.setOnClickListener {
            currentQuestion++
            if (currentQuestion > 9){
                navigateFinishScreen()
            }
            else{
                updateQuestion()
                setClickListeners()
                timer.start()
                binding.btnNext.visibility = View.GONE
            }
        }

        binding.cvExtraTime.setOnClickListener {
            timer.cancel()
            extraTime = ANSWER_TIME - usedTime.toLong() + EXTRA_TIME
            startExtraTimer()
            binding.ivExtraTimeCross.visibility = View.VISIBLE
            binding.cvExtraTime.isClickable = false
        }

        binding.cvFiftyFifty.setOnClickListener {
            activateFiftyFifty()
            binding.ivFiftyFiftyCross.visibility = View.VISIBLE
            binding.cvFiftyFifty.isClickable = false
        }

        return binding.root
    }

    private fun navigateFinishScreen() {
        val avgTime = calculateAvgTime()
        val args = Bundle()
        args.putInt("score", totalScore)
        args.putString("time", avgTime)
        binding.root.findNavController().navigate(R.id.action_questionFragment_to_resultFragment,args)
    }

    private fun activateFiftyFifty() {
        val listWrongAnswers = ArrayList<String>()
        questions?.get(currentQuestion)?.let {
            it.incorrectAnswers.forEach { incorrectAnswer->
                listWrongAnswers.add(incorrectAnswer)
            }
        }

        val randomIndex = Random.nextInt(listWrongAnswers.size)
        listWrongAnswers.removeAt(randomIndex)

        listWrongAnswers.forEach {
            when(it){
                answer1 -> {
                    binding.tvAnswer1.text = ""
                    binding.cvAnswer1.isClickable = false
                }
                answer2 -> {
                    binding.tvAnswer2.text = ""
                    binding.cvAnswer2.isClickable = false
                }
                answer3 -> {
                    binding.tvAnswer3.text = ""
                    binding.cvAnswer3.isClickable = false
                }
                answer4 -> {
                    binding.tvAnswer4.text = ""
                    binding.cvAnswer4.isClickable = false
                }
            }
        }
    }

    private fun startExtraTimer() {
        extraTimerIsRunning = true
        extraTimer = object: CountDownTimer(extraTime, 1) {
            override fun onTick(millisUntilFinished: Long) {
                binding.pbTime.progress = (millisUntilFinished.toInt() * 100 /extraTime.toInt())
                usedExtraTime = (ANSWER_TIME + EXTRA_TIME).toInt() - millisUntilFinished.toInt()
            }

            override fun onFinish() {
                answerTimes.add((ANSWER_TIME + EXTRA_TIME).toInt())
                extraTimerIsRunning = false
                showCorrectAnswer(true)
                finishQuestion()
            }
        }
        extraTimer.start()
    }

    private fun calculateAvgTime(): String {
        val timeAvg = answerTimes.sum().toDouble() / 10000
        return String.format("%.2f", timeAvg) + "s"
    }

    private fun setClickListeners() {

        binding.cvAnswer1.setOnClickListener {
            handleTimers()
            if (answer1 == correctAnswer){
                binding.cvAnswer1.setCardBackgroundColor(resources.getColor(R.color.green))
                totalScore++
            }
            else{
                binding.cvAnswer1.setCardBackgroundColor(resources.getColor(R.color.red))
                showCorrectAnswer()
            }
            finishQuestion()
        }

        binding.cvAnswer2.setOnClickListener {
            handleTimers()
            if (answer2 == correctAnswer){
                binding.cvAnswer2.setCardBackgroundColor(resources.getColor(R.color.green))
                totalScore++
            }
            else{
                binding.cvAnswer2.setCardBackgroundColor(resources.getColor(R.color.red))
                showCorrectAnswer()
            }
            finishQuestion()
        }

        binding.cvAnswer3.setOnClickListener {
            handleTimers()
            if (answer3 == correctAnswer){
                binding.cvAnswer3.setCardBackgroundColor(resources.getColor(R.color.green))
                totalScore++
            }
            else{
                binding.cvAnswer3.setCardBackgroundColor(resources.getColor(R.color.red))
                showCorrectAnswer()
            }
            finishQuestion()
        }

        binding.cvAnswer4.setOnClickListener {
            handleTimers()
            if (answer4 == correctAnswer){
                binding.cvAnswer4.setCardBackgroundColor(resources.getColor(R.color.green))
                totalScore++
            }
            else{
                binding.cvAnswer4.setCardBackgroundColor(resources.getColor(R.color.red))
                showCorrectAnswer()
            }
            finishQuestion()
        }

    }

    private fun handleTimers() {
        if (extraTimerIsRunning){
            extraTimer.cancel()
            answerTimes.add(usedExtraTime)
            extraTimerIsRunning = false
        }
        else{
            timer.cancel()
            answerTimes.add(usedTime)
        }
    }

    private fun showCorrectAnswer(outOfTime: Boolean = false) {

        var color = if (outOfTime) R.color.red else R.color.green

        when(correctAnswer){
            answer1 -> {
                binding.cvAnswer1.setCardBackgroundColor(resources.getColor(color))
            }
            answer2 -> {
                binding.cvAnswer2.setCardBackgroundColor(resources.getColor(color))
            }
            answer3 -> {
                binding.cvAnswer3.setCardBackgroundColor(resources.getColor(color))
            }
            answer4 -> {
                binding.cvAnswer4.setCardBackgroundColor(resources.getColor(color))
            }
        }
    }

    private fun finishQuestion() {
        binding.also {
            it.cvAnswer1.isClickable = false
            it.cvAnswer2.isClickable = false
            it.cvAnswer3.isClickable = false
            it.cvAnswer4.isClickable = false
            it.cvExtraTime.isClickable = false
            it.cvFiftyFifty.isClickable = false
            it.btnNext.visibility = View.VISIBLE
        }
    }


    private fun setViewsVisible() {
        binding.also {
            it.pbLoading.visibility = View.GONE
            it.tvQuestionNumber.visibility = View.VISIBLE
            it.tvQuestion.visibility = View.VISIBLE
            it.cvAnswer1.visibility = View.VISIBLE
            it.cvAnswer2.visibility = View.VISIBLE
            it.cvAnswer3.visibility = View.VISIBLE
            it.cvAnswer4.visibility = View.VISIBLE
            it.pbTime.visibility = View.VISIBLE
            it.cvFiftyFifty.visibility = View.VISIBLE
            it.cvExtraTime.visibility = View.VISIBLE
            it.cvQuestion.visibility = View.VISIBLE
        }
    }

    private fun updateQuestion() {

        val answers = arrayListOf<String>()
        var question = ""

        prepareAnswersNextRound()
        prepareLifeLineNextRound()

        questions?.get(currentQuestion)?.let {
            question = it.question
            correctAnswer = it.correctAnswer
            answers.add(correctAnswer)
            it.incorrectAnswers.forEach { incorrectAnswer->
                answers.add(incorrectAnswer)
            }
        }
        answers.shuffle()

        answer1 = answers[0]
        answer2 = answers[1]
        answer3 = answers[2]
        answer4 = answers[3]

        binding.also {
            it.tvQuestionNumber.text = "Question " + (currentQuestion+1) + "/10"
            it.tvQuestion.text = Html.fromHtml(question, Html.FROM_HTML_MODE_COMPACT).toString()
            it.tvAnswer1.text = Html.fromHtml(answer1, Html.FROM_HTML_MODE_COMPACT).toString()
            it.tvAnswer2.text = Html.fromHtml(answer2, Html.FROM_HTML_MODE_COMPACT).toString()
            it.tvAnswer3.text = Html.fromHtml(answer3, Html.FROM_HTML_MODE_COMPACT).toString()
            it.tvAnswer4.text = Html.fromHtml(answer4, Html.FROM_HTML_MODE_COMPACT).toString()
        }
    }

    private fun prepareLifeLineNextRound() {
        binding.also {
            if (it.ivExtraTimeCross.visibility != View.VISIBLE){
                it.cvExtraTime.isClickable = true
            }
            if (it.ivFiftyFiftyCross.visibility != View.VISIBLE){
                it.cvFiftyFifty.isClickable = true
            }
        }
    }

    private fun prepareAnswersNextRound() {
        binding.also {
            it.cvAnswer1.isClickable = true
            it.cvAnswer2.isClickable = true
            it.cvAnswer3.isClickable = true
            it.cvAnswer4.isClickable = true
            it.cvAnswer1.setCardBackgroundColor(resources.getColor(R.color.white))
            it.cvAnswer2.setCardBackgroundColor(resources.getColor(R.color.white))
            it.cvAnswer3.setCardBackgroundColor(resources.getColor(R.color.white))
            it.cvAnswer4.setCardBackgroundColor(resources.getColor(R.color.white))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        if (extraTimerIsRunning) extraTimer.cancel()
    }

}