package com.example.coodeshchallenge_wordsearch.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.coodeshchallenge_wordsearch.data.sources.DictionaryProvider
import com.example.coodeshchallenge_wordsearch.ui.BaseViewModel
import kotlinx.coroutines.launch

class HistoryViewModel(private val dictionaryProviderImpl: DictionaryProvider) : BaseViewModel() {

    private val _previouslySearchedWords = MutableLiveData<List<String>>()
    val previouslySearchedWords: LiveData<List<String>>
        get() = _previouslySearchedWords

    init {
        getPreviouslySearchedWords()
    }

    private fun getPreviouslySearchedWords() {
        viewModelScope.launch {
            _previouslySearchedWords.value = dictionaryProviderImpl.getPreviouslySearchedWords().map { it.word }
        }
    }

    fun removeWordFromSearchHistory(word: String) {
        viewModelScope.launch {
            dictionaryProviderImpl.removeWordFromSearchHistory(word)
        }
    }

    companion object {
        const val TAG = "HistoryViewModel"
    }
}
