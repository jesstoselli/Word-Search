package com.example.coodeshchallenge_wordsearch.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.coodeshchallenge_wordsearch.databinding.FragmentDictionaryBinding
import com.example.coodeshchallenge_wordsearch.ui.DictionaryViewModel
import com.example.coodeshchallenge_wordsearch.ui.fragments.adapters.WordsListAdapter
import org.koin.android.ext.android.inject

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
                val action = DictionaryFragmentDirections.actionNavigationDictionaryToWordPageFragment(word.capitalize())
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

//        binding.btnDictionaryToWordPage.setOnClickListener {
//            val arg = "Banana"
//            val action = DictionaryFragmentDirections.actionNavigationDictionaryToWordPageFragment(arg)
//            findNavController().navigate(action)
//        }
