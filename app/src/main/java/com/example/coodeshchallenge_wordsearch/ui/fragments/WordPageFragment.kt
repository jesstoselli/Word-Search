package com.example.coodeshchallenge_wordsearch.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.coodeshchallenge_wordsearch.databinding.FragmentWordPageBinding
import com.example.coodeshchallenge_wordsearch.ui.DictionaryViewModel
import com.example.coodeshchallenge_wordsearch.ui.fragments.adapters.MeaningsListAdapter
import com.example.coodeshchallenge_wordsearch.utils.ApiStatus
import org.koin.android.ext.android.inject

class WordPageFragment : Fragment() {

    private val viewModel: DictionaryViewModel by inject()

    private lateinit var meaningsAdapter: MeaningsListAdapter

    private var _binding: FragmentWordPageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWordPageBinding.inflate(inflater, container, false)

        viewModel.apiStatus.observe(viewLifecycleOwner, Observer { apiStatus ->
            with(binding) {
                when (apiStatus) {
                    ApiStatus.LOADING -> {

                    }
                    ApiStatus.DONE -> {

                    }
                    ApiStatus.ERROR -> {
                        WordPageFragmentDirections.actionWordPageFragmentToErrorFragment()
                    }
                }
            }
        })

        viewModel.chosenWord.observe(viewLifecycleOwner, Observer { wordDTO ->
            with(binding) {
                meaningsAdapter = MeaningsListAdapter(wordDTO.meanings)
                recyclerViewMeaningsList.adapter = MeaningsListAdapter(wordDTO.meanings)
                textViewWord.text = wordDTO.word
                textViewPhonetic.text = wordDTO.phoneticsText

            }
        })

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnNext.setOnClickListener {
            Toast.makeText(requireContext(), "Next word", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
