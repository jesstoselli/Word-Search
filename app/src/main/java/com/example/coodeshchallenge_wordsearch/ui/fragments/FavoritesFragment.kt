package com.example.coodeshchallenge_wordsearch.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.coodeshchallenge_wordsearch.databinding.FragmentFavoritesBinding
import com.example.coodeshchallenge_wordsearch.ui.DictionaryViewModel
import com.example.coodeshchallenge_wordsearch.ui.fragments.adapters.WordsListAdapter
import com.example.coodeshchallenge_wordsearch.utils.toFirstCapitalLetters
import org.koin.android.ext.android.inject

class FavoritesFragment : Fragment() {

    private val viewModel: DictionaryViewModel by inject()

    private var _binding: FragmentFavoritesBinding? = null

    private lateinit var adapter: WordsListAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        adapter = WordsListAdapter(WordsListAdapter.WordClickedListener { word ->
            viewModel.navigateToWordPage(word)
        })

        viewModel.navigateToWordPage.observe(viewLifecycleOwner, Observer { word ->
            if (word != null) {
                val action =
                    FavoritesFragmentDirections.actionNavigationFavoritesToWordPageFragment(word.toFirstCapitalLetters(word))
                findNavController().navigate(action)

                viewModel.returnFromWordPage()
            }
        })

        viewModel.wordsList.observe(viewLifecycleOwner, Observer { wordsList ->
            adapter.submitList(wordsList)
            binding.recyclerViewFavorites.adapter = adapter
        })

        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
