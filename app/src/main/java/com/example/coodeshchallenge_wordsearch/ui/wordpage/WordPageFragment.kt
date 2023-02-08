package com.example.coodeshchallenge_wordsearch.ui.wordpage

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.coodeshchallenge_wordsearch.R
import com.example.coodeshchallenge_wordsearch.databinding.FragmentWordPageBinding
import com.example.coodeshchallenge_wordsearch.ui.model.WordDTO
import com.example.coodeshchallenge_wordsearch.utils.ApiStatus
import org.koin.android.ext.android.inject
import java.io.IOException

class WordPageFragment : Fragment() {

    private val viewModel: WordPageViewModel by inject()

    private val args: WordPageFragmentArgs by navArgs()

    private var _binding: FragmentWordPageBinding? = null

    private var mediaPlayer: MediaPlayer? = null

    private lateinit var meaningsAdapter: MeaningsListAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWordPageBinding.inflate(layoutInflater)

        val originFragment = args.origin

        viewModel.getWordDefinition(args.title)

        viewModel.apiStatus.observe(viewLifecycleOwner, Observer { apiStatus ->

            val wordDto = viewModel.selectedWord.value

            when (apiStatus) {
                ApiStatus.LOADING -> {
                    Log.i("WordPageFragment", "ApiStatus is Loading")

                    binding.progressIndicator.visibility = View.VISIBLE
                    binding.scrollViewWordPage.visibility = View.GONE
                }

                ApiStatus.SUCCESS -> {
                    if (wordDto != null) {
                        activity?.title = wordDto.word
                        viewModel.setFavorite(wordDto.favorite)
                        setUpUI(wordDto)
                    }
                }

                ApiStatus.ERROR -> {
                    val action =
                        WordPageFragmentDirections.actionWordPageFragmentToErrorFragment(viewModel.apiErrorMessage)
                    findNavController().navigate(action)
                }

                else -> Log.i("WordPageFragment", "ApiStatus is Loading")
            }

        })

        with(binding) {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnNext.setOnClickListener {

                Log.i(TAG, "inside btnNext click listener, origin fragment is $originFragment")

                when (originFragment) {
                    "Dictionary" -> viewModel.getRandomWordDefinition()
                    "History" -> viewModel.getRandomPreviouslySearchedWordEntry()
                    "Favorites" -> viewModel.getRandomPreviouslySearchedWordEntry()
                }
            }

            imageViewToggleFavoriteWord.setOnClickListener {
                viewModel.selectedWord.value?.let { word -> toggleFavorite(word) }
            }
        }

        return binding.root
    }

    private fun toggleFavorite(wordDTO: WordDTO) {
        val isFavorite = viewModel.isWordFavorite.value!!

        viewModel.toggleFavoriteWord(wordDTO.word, !isFavorite)

        if (isFavorite) {
            binding.imageViewToggleFavoriteWord.setImageResource(R.drawable.ic_heart_outline)
        } else {
            binding.imageViewToggleFavoriteWord.setImageResource(R.drawable.ic_heart_filled)
        }

        viewModel.setFavorite(!isFavorite)
    }

    private fun setUpUI(wordDTO: WordDTO) {
        with(binding) {
            progressIndicator.visibility = View.GONE
            scrollViewWordPage.visibility = View.VISIBLE

            meaningsAdapter = MeaningsListAdapter(wordDTO.meanings)
            recyclerViewMeaningsList.adapter = meaningsAdapter
            textViewWord.text = wordDTO.word
            textViewPhonetic.text = wordDTO.phoneticsText

            if (wordDTO.favorite) {
                imageViewToggleFavoriteWord.setImageResource(R.drawable.ic_heart_filled)
            } else {
                imageViewToggleFavoriteWord.setImageResource(R.drawable.ic_heart_outline)
            }

            val audio = wordDTO.phoneticsAudio

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


    companion object {
        const val TAG = "WordPageFragment"
    }
}
