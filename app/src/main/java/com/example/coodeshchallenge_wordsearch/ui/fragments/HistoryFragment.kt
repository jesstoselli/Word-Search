package com.example.coodeshchallenge_wordsearch.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.coodeshchallenge_wordsearch.databinding.FragmentHistoryBinding
import com.example.coodeshchallenge_wordsearch.ui.fragments.adapters.WordsListAdapter
import com.example.coodeshchallenge_wordsearch.utils.toFirstCapitalLetters
import org.koin.android.ext.android.inject

class HistoryFragment : Fragment() {

    private val viewModel: HistoryViewModel by inject()

    private var _binding: FragmentHistoryBinding? = null

    private lateinit var adapter: WordsListAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        adapter = WordsListAdapter(WordsListAdapter.WordClickedListener { word ->
            Log.i("HistoryFragment", "inside WordClickedListener. Word is $word")
            viewModel.navigateToWordPage(word)
        })

        viewModel.navigateToWordPage.observe(viewLifecycleOwner, Observer { word ->
            if (word != null) {
                viewModel.getPreviouslySearchedWordEntry(word)

                Log.i("HistoryFragment", "inside navigateToWordPage observable. Word is $word")
                val action =
                    HistoryFragmentDirections.actionNavigationHistoryToWordPageFragment(word.toFirstCapitalLetters(word))
                findNavController().navigate(action)

                viewModel.returnFromWordPage()
            }
        })

        viewModel.previouslySearchedWords.observe(viewLifecycleOwner, Observer { wordsList ->
            adapter.submitList(wordsList)
            binding.recyclerViewHistory.adapter = adapter
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
