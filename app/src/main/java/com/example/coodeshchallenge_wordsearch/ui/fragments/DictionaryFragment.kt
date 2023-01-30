package com.example.coodeshchallenge_wordsearch.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coodeshchallenge_wordsearch.databinding.FragmentDictionaryBinding
import com.example.coodeshchallenge_wordsearch.ui.DictionaryViewModel
import com.example.coodeshchallenge_wordsearch.ui.fragments.adapters.PagedWordListAdapter
import com.example.coodeshchallenge_wordsearch.utils.toFirstCapitalLetters
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class DictionaryFragment : Fragment() {

    private val viewModel: DictionaryViewModel by inject()

    private var _binding: FragmentDictionaryBinding? = null

    private lateinit var pagingAdapter: PagedWordListAdapter
    private lateinit var dictionaryRecyclerView: RecyclerView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDictionaryBinding.inflate(inflater, container, false)

        dictionaryRecyclerView = binding.recyclerViewDictionary

        viewModel.populateDatabaseFromFile(requireContext(), "words.txt")

        pagingAdapter = PagedWordListAdapter(PagedWordListAdapter.PagedWordClickedListener { dictionaryEntry ->
            viewModel.navigateToWordPage(dictionaryEntry.word)
        })

        binding.recyclerViewDictionary.adapter = pagingAdapter
        lifecycleScope.launch {
            viewModel.wordsFlow.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }

        binding.recyclerViewDictionary.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                if (lastVisibleItem + 1 == pagingAdapter.itemCount) {
                    viewModel.getWordsFromDatabase()
                }
            }
        })

        viewModel.navigateToWordPage.observe(viewLifecycleOwner, Observer { word ->
            if (word != null) {
                val action =
                    DictionaryFragmentDirections.actionNavigationDictionaryToWordPageFragment(word.toFirstCapitalLetters(word))
                findNavController().navigate(action)

                viewModel.returnFromWordPage()
            }
        })



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
