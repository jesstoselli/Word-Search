package com.example.coodeshchallenge_wordsearch.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.coodeshchallenge_wordsearch.databinding.FragmentDictionaryBinding
import com.example.coodeshchallenge_wordsearch.ui.DictionaryViewModel
import com.example.coodeshchallenge_wordsearch.ui.fragments.adapters.WordsListAdapter
import com.example.coodeshchallenge_wordsearch.utils.toFirstCapitalLetters
import org.koin.android.ext.android.inject
import java.util.*

class DictionaryFragment : Fragment() {

    private val viewModel: DictionaryViewModel by inject()

    private var _binding: FragmentDictionaryBinding? = null

    private lateinit var adapter: WordsListAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDictionaryBinding.inflate(inflater, container, false)

        adapter = WordsListAdapter(WordsListAdapter.WordClickedListener { word ->
            viewModel.navigateToWordPage(word)
        })

        viewModel.navigateToWordPage.observe(viewLifecycleOwner, Observer { word ->
            if (word != null) {
                val action =
                    DictionaryFragmentDirections.actionNavigationDictionaryToWordPageFragment(word.toFirstCapitalLetters(word))
                findNavController().navigate(action)

                viewModel.returnFromWordPage()
            }
        })

        viewModel.wordsList.observe(viewLifecycleOwner, Observer { wordsList ->
            adapter.submitList(wordsList)
            binding.recyclerViewDictionary.adapter = adapter
        })

//        viewModel.populateDatabaseFromFile(requireContext(), "words.txt")

        return binding.root
    }

    private fun capitalizeEachWord(word: String): String {
        return word.substring(0, 1).uppercase(Locale.getDefault()) + word.substring(1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
