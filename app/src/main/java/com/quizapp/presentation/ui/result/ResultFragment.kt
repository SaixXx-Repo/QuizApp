package com.quizapp.presentation.ui.result

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.quizapp.R
import com.quizapp.databinding.FragmentResultBinding
import com.quizapp.databinding.FragmentStartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment : Fragment() {

    private var _binding : FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)

        val args = arguments
        val score = args?.getInt("score")
        val time = args?.getString("time")

        binding.tvScore.text = score.toString() + " / 10"
        binding.tvTime.text = time

        binding.btnFinish.setOnClickListener {
            it.findNavController().navigate(R.id.action_resultFragment_to_startFragment)
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}