package com.example.coodeshchallenge_wordsearch.ui.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.coodeshchallenge_wordsearch.R
import com.example.coodeshchallenge_wordsearch.databinding.FragmentWordPageBinding
import com.example.coodeshchallenge_wordsearch.ui.BaseViewModel
import com.example.coodeshchallenge_wordsearch.ui.DictionaryViewModel
import com.example.coodeshchallenge_wordsearch.ui.fragments.adapters.MeaningsListAdapter
import com.example.coodeshchallenge_wordsearch.ui.model.WordDTO
import com.example.coodeshchallenge_wordsearch.utils.ApiStatus
import kotlinx.coroutines.delay
import org.koin.android.ext.android.inject
import java.io.IOException

class WordPageFragment : Fragment() {

    private val baseViewModel: BaseViewModel by inject()
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

        Log.i("WordPageFragment", "Inside onCreateView")

        val originFragment = args.origin

        dictionaryViewModel.getWordDefinition(args.title)

        val statusToObserve = if (originFragment == "Dictionary") {
            dictionaryViewModel.dictionaryApiStatus
        } else {
            historyViewModel.historyApiStatus
        }

        statusToObserve.observe(viewLifecycleOwner, Observer { apiStatus ->

            Log.i("WordPageFragment", "baseViewModel.apiStatus")

            when (apiStatus) {
                is ApiStatus.Loading -> {
                    Log.i("WordPageFragment", "ApiStatus is Loading")
                }

                is ApiStatus.Success -> {
                    Log.i("WordPageFragment", "ApiStatus is Success")

                    if (apiStatus.data != null) {
                        baseViewModel.setFavorite(apiStatus.data.favorite)
                        setUpUI(apiStatus)

                    } else {
                        if (statusToObserve === dictionaryViewModel.dictionaryApiStatus) {
                            dictionaryViewModel.setApiStatus(
                                (ApiStatus.Error(
                                    message = R.string.error_message_no_word_definition.toString()
                                ))
                            )
                        } else {
//                            historyViewModel.se
                        }
                    }
                }

                is ApiStatus.Error -> {
                    Log.i("WordPageFragment", "ApiStatus is error")
                    val message = apiStatus.message
                    if (message.isNullOrEmpty()) {
                        val action = WordPageFragmentDirections.actionWordPageFragmentToErrorFragment()
                        findNavController().navigate(action)
                    } else {
                        val action =
                            WordPageFragmentDirections.actionWordPageFragmentToErrorFragment(message)
                        findNavController().navigate(action)

                    }
                }
            }
        })

        with(binding) {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnNext.setOnClickListener {
                if (originFragment === "Dictionary") {
                    dictionaryViewModel.getRandomWordDefinition()
                } else {
                    historyViewModel.getRandomPreviouslySearchedWordEntry()
                }
            }

            imageViewToggleFavoriteWord.setOnClickListener {
                toggleFavorite(statusToObserve)
            }
        }

        return binding.root
    }

    private fun toggleFavorite(status: LiveData<ApiStatus<WordDTO>>) {
        val currentWord = status.value!!.data!!.word

        val isFavorite = baseViewModel.isWordFavorite.value!!

        historyViewModel.toggleFavoriteWord(currentWord, !isFavorite)

        if (isFavorite) {
            binding.imageViewToggleFavoriteWord.setImageResource(R.drawable.ic_heart_outline)
        } else {
            binding.imageViewToggleFavoriteWord.setImageResource(R.drawable.ic_heart_filled)
        }
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


//baseViewModel.apiStatus.observe(viewLifecycleOwner, Observer { apiStatus ->
//
//    Log.i("WordPageFragment", "baseViewModel.apiStatus")
//
//    when (apiStatus) {
//        is ApiStatus.Loading -> {
//            Log.i("WordPageFragment", "ApiStatus is Loading")
//        }
//
//        is ApiStatus.Success -> {
//            Log.i("WordPageFragment", "ApiStatus is Success")
//
//            if (apiStatus.data != null && apiStatus.data.word.isNotEmpty()) {
//                baseViewModel.setFavorite(apiStatus.data.favorite)
//                setUpUI(apiStatus)
//
//            } else {
////                            baseViewModel.setApiStatus(ApiStatus.Error(message = apiStatus.data.))
//                val action =
//                    WordPageFragmentDirections.actionWordPageFragmentToErrorFragment()
//                findNavController().navigate(action)
//
//            }
//        }
//
//        is ApiStatus.Error -> {
//            Log.i("WordPageFragment", "ApiStatus is error")
//            val message = apiStatus.message
//            if (message.isNullOrEmpty()) {
//                val action = WordPageFragmentDirections.actionWordPageFragmentToErrorFragment()
//                findNavController().navigate(action)
//            } else {
//                val action =
//                    WordPageFragmentDirections.actionWordPageFragmentToErrorFragment(message)
//                findNavController().navigate(action)
//
//            }
//        }
//    }
//})
