package com.example.coodeshchallenge_wordsearch.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.coodeshchallenge_wordsearch.R
import com.example.coodeshchallenge_wordsearch.databinding.FragmentWordPageBinding
import com.example.coodeshchallenge_wordsearch.ui.DictionaryViewModel
import com.example.coodeshchallenge_wordsearch.ui.fragments.adapters.MeaningsListAdapter
import com.example.coodeshchallenge_wordsearch.utils.ApiStatus
import org.koin.android.ext.android.inject

class WordPageFragment : Fragment() {

    private val viewModel: DictionaryViewModel by inject()
    private val args: WordPageFragmentArgs by navArgs()

    private var _binding: FragmentWordPageBinding? = null

    private lateinit var meaningsAdapter: MeaningsListAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWordPageBinding.inflate(layoutInflater)

        viewModel.getWordDefinitionFromAPI(args.title)

        with(binding) {
            viewModel.apiStatus.observe(viewLifecycleOwner, Observer { apiStatus ->


                when (apiStatus) {
                    is ApiStatus.Loading -> {
                        progressIndicator.visibility = View.VISIBLE
                        Log.i("WordPageFragment", "ApiStatus is Loading ")
                    }
                    is ApiStatus.Success -> {
                        Log.i("WordPageFragment", "ApiStatus is Success ")
                        if (apiStatus.data != null) {
                            meaningsAdapter = MeaningsListAdapter(apiStatus.data.meanings)
                            recyclerViewMeaningsList.adapter = meaningsAdapter
                            textViewWord.text = apiStatus.data.word
                            textViewPhonetic.text = apiStatus.data.phoneticsText

                            if (apiStatus.data.favorite) {
                                imageViewToggleFavoriteWord.setImageResource(R.drawable.ic_heart_filled)
                            } else {
                                imageViewToggleFavoriteWord.setImageResource(R.drawable.ic_heart_outline)
                            }

                            progressIndicator.visibility = View.GONE
                            scrollViewWordPage.visibility = View.VISIBLE
                        } else {
                            WordPageFragmentDirections.actionWordPageFragmentToErrorFragment()
                        }
                    }
                    is ApiStatus.Error -> {
                        Log.i("WordPageFragment", "ApiStatus is error ")
                        WordPageFragmentDirections.actionWordPageFragmentToErrorFragment()
                    }
                }
            })

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnNext.setOnClickListener {
                Toast.makeText(requireContext(), "Next word", Toast.LENGTH_SHORT).show()
            }

            imageViewToggleFavoriteWord.setOnClickListener {
                if (viewModel.apiStatus.value?.data != null) {
                    viewModel.toggleFavoriteWord(args.title, !viewModel.apiStatus.value?.data!!.favorite)
                }
            }


        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
