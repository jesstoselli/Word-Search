package com.example.coodeshchallenge_wordsearch.ui.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.coodeshchallenge_wordsearch.R
import com.example.coodeshchallenge_wordsearch.databinding.FragmentErrorBinding

class ErrorFragment : Fragment() {

    private var _binding: FragmentErrorBinding? = null

    private val args: ErrorFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentErrorBinding.inflate(inflater, container, false)

        binding.textViewErrorMessage.text = if (args.errorMessage == "404") {
            getString(R.string.error_message_no_word_definition)
        } else getString(R.string.error_message_default)

        binding.btnReturn.setOnClickListener {
            val action =
                ErrorFragmentDirections.actionNavigationErrorToNavigationDictionary()
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
