package com.example.coodeshchallenge_wordsearch.ui.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.coodeshchallenge_wordsearch.R
import com.example.coodeshchallenge_wordsearch.databinding.FragmentWordPageBinding
import com.example.coodeshchallenge_wordsearch.ui.DictionaryViewModel
import com.example.coodeshchallenge_wordsearch.ui.fragments.adapters.MeaningsListAdapter
import com.example.coodeshchallenge_wordsearch.ui.model.WordDTO
import com.example.coodeshchallenge_wordsearch.utils.ApiStatus
import org.koin.android.ext.android.inject
import java.io.IOException

class WordPageFragment : Fragment() {

    private val dictionaryViewModel: DictionaryViewModel by inject()
    private val historyViewModel: HistoryViewModel by inject()
    private val args: WordPageFragmentArgs by navArgs()

    private var _binding: FragmentWordPageBinding? = null

    private var mediaPlayer: MediaPlayer? = null

    private lateinit var meaningsAdapter: MeaningsListAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWordPageBinding.inflate(layoutInflater)

        val originFragment = args.origin

        with(binding) {

            val statusToObserve = if (originFragment == "Dictionary") {
                dictionaryViewModel.dictionaryApiStatus
            } else {
                historyViewModel.historyApiStatus
            }

            if (statusToObserve == historyViewModel.historyApiStatus) {
                historyViewModel.getPreviouslySearchedWordEntry(args.title)
            } else {
                dictionaryViewModel.getWordDefinitionFromAPI(args.title)
            }


            statusToObserve.observe(viewLifecycleOwner, Observer { apiStatus ->

                when (apiStatus) {
                    is ApiStatus.Loading -> {
                        Log.i("WordPageFragment", "ApiStatus is Loading ")
                    }
                    is ApiStatus.Success -> {
                        Log.i("WordPageFragment", "ApiStatus is Success ")
                        if (apiStatus.data != null && apiStatus.data.word.isNotEmpty()) {
                            setUpUI(apiStatus)
                        } else {
                            val action =
                                WordPageFragmentDirections.actionWordPageFragmentToErrorFragment()
                            findNavController().navigate(action)
                        }
                    }
                    is ApiStatus.Error -> {
                        Log.i("WordPageFragment", "ApiStatus is error ")
                        val action =
                            WordPageFragmentDirections.actionWordPageFragmentToErrorFragment()
                        findNavController().navigate(action)
                    }
                }
            })

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnNext.setOnClickListener {
                historyViewModel.getRandomPreviouslySearchedWordEntry()
            }

            imageViewToggleFavoriteWord.setOnClickListener {
                val loadedData = statusToObserve.value?.data

                if (loadedData != null) {
                    val isFavorite = statusToObserve.value?.data!!.favorite

                    historyViewModel.toggleFavoriteWord(loadedData.word, !isFavorite)

                    if (isFavorite) {
                        imageViewToggleFavoriteWord.setImageResource(R.drawable.ic_heart_outline)

                    } else {
                        imageViewToggleFavoriteWord.setImageResource(R.drawable.ic_heart_filled)
                    }

                }
            }
        }

        return binding.root
    }

    private fun setUpUI(apiStatus: ApiStatus<WordDTO>) {
        with(binding) {
            progressIndicator.visibility = View.GONE
            scrollViewWordPage.visibility = View.VISIBLE

            meaningsAdapter = MeaningsListAdapter(apiStatus.data!!.meanings)
            recyclerViewMeaningsList.adapter = meaningsAdapter
            textViewWord.text = apiStatus.data.word
            textViewPhonetic.text = apiStatus.data.phoneticsText

            if (apiStatus.data.favorite) {
                imageViewToggleFavoriteWord.setImageResource(R.drawable.ic_heart_filled)
            } else {
                imageViewToggleFavoriteWord.setImageResource(R.drawable.ic_heart_outline)
            }

            val audio = apiStatus.data.phoneticsAudio

            if (audio.isNullOrEmpty()) {
                textViewPhoneticsNoAudio.visibility = View.VISIBLE
                btnPlay.visibility = View.GONE
            } else {
                btnPlay.setOnClickListener {
                    try {
                        mediaPlayer = MediaPlayer()
                        mediaPlayer?.apply {
                            setDataSource(audio)
                            prepareAsync()
                            setOnPreparedListener {
                                it.start()
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.apply {
            stop()
            reset()
            release()
        }
        _binding = null
    }
}
