package com.quizapp.presentation.ui.start

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.quizapp.R
import com.quizapp.common.Constants.CATEGORIES
import com.quizapp.common.Constants.CATEGORY_ID
import com.quizapp.databinding.FragmentStartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartFragment : Fragment() {

    private var _binding : FragmentStartBinding? = null
    private val binding get() = _binding!!

    private var selectedCategory: String = ""
    private var selectedDifficulty: String = "medium"

    private val TAG = "StartFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)

        binding.btnStart.setOnClickListener {
            val args = Bundle()
            Log.i(TAG,"Category: $selectedCategory Difficulty: $selectedDifficulty")
            args.putString("category", selectedCategory)
            args.putString("difficulty", selectedDifficulty)
            it.findNavController().navigate(R.id.action_startFragment_to_questionFragment,args)
        }

        binding.btnEasy.setOnClickListener {
            updateSelectedCardView("easy")
        }
        binding.btnMedium.setOnClickListener {
            updateSelectedCardView("medium")
        }
        binding.btnHard.setOnClickListener {
            updateSelectedCardView("hard")
        }

        setupSpinner()

        return binding.root
    }

    private fun updateSelectedCardView(difficulty: String = "medium") {
        selectedDifficulty = difficulty

        when(difficulty){
            "easy" -> {
                binding.btnEasy.setStrokeColorResource(R.color.teal_200)
                binding.btnMedium.setStrokeColorResource(R.color.white)
                binding.btnHard.setStrokeColorResource(R.color.white)
                binding.btnEasy.setTextColor(resources.getColor(R.color.teal_200))
                binding.btnMedium.setTextColor(resources.getColor(R.color.white))
                binding.btnHard.setTextColor(resources.getColor(R.color.white))
            }
            "medium" -> {
                binding.btnEasy.setStrokeColorResource(R.color.white)
                binding.btnMedium.setStrokeColorResource(R.color.teal_200)
                binding.btnHard.setStrokeColorResource(R.color.white)
                binding.btnEasy.setTextColor(resources.getColor(R.color.white))
                binding.btnMedium.setTextColor(resources.getColor(R.color.teal_200))
                binding.btnHard.setTextColor(resources.getColor(R.color.white))
            }
            "hard" -> {
                binding.btnEasy.setStrokeColorResource(R.color.white)
                binding.btnMedium.setStrokeColorResource(R.color.white)
                binding.btnHard.setStrokeColorResource(R.color.teal_200)
                binding.btnEasy.setTextColor(resources.getColor(R.color.white))
                binding.btnMedium.setTextColor(resources.getColor(R.color.white))
                binding.btnHard.setTextColor(resources.getColor(R.color.teal_200))
            }
        }
    }


    /**
     * Sets up spinner for different seasons of the series
     */
    private fun setupSpinner() = binding.spCategory.apply {

        adapter = ArrayAdapter(binding.root.context,android.R.layout.simple_list_item_1,CATEGORIES)

        onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategory = CATEGORY_ID[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedCategory = CATEGORY_ID[0]
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}