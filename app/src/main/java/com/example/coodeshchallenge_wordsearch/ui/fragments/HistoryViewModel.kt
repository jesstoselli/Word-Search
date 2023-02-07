package com.example.coodeshchallenge_wordsearch.ui.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.coodeshchallenge_wordsearch.data.sources.DictionaryProvider
import com.example.coodeshchallenge_wordsearch.ui.BaseViewModel
import com.example.coodeshchallenge_wordsearch.ui.model.WordDTO
import com.example.coodeshchallenge_wordsearch.utils.ApiStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HistoryViewModel(private val dictionaryProviderImpl: DictionaryProvider) : BaseViewModel() {

    private val _historyApiStatus = MutableLiveData<ApiStatus<WordDTO>>()
    val historyApiStatus: LiveData<ApiStatus<WordDTO>>
        get() = _historyApiStatus

    private val _favoriteWords = MutableLiveData<List<String>>()
    val favoriteWords: LiveData<List<String>>
        get() = _favoriteWords

    private val _previouslySearchedWords = MutableLiveData<List<String>>()
    val previouslySearchedWords: LiveData<List<String>>
        get() = _previouslySearchedWords

    init {
        getFavoriteWordsFromDatabase()
        getPreviouslySearchedWords()
    }

    fun toggleFavoriteWord(word: String, isFavorite: Boolean) {
        viewModelScope.launch {
            dictionaryProviderImpl.toggleFavoriteWord(word, isFavorite)
        }
    }

    fun removeWordFromSearchHistory(word: String) {
        viewModelScope.launch {
            dictionaryProviderImpl.removeWordFromSearchHistory(word)
        }
    }

    fun getRandomPreviouslySearchedWordEntry() {
        viewModelScope.launch {
            val word = dictionaryProviderImpl.getRandomPreviouslySearchedWordEntry()
            word?.let { _historyApiStatus.value = ApiStatus.Success(word) }
        }
    }

    private fun getPreviouslySearchedWords() {
        viewModelScope.launch {
            _previouslySearchedWords.value = dictionaryProviderImpl.getPreviouslySearchedWords().map { it.word }
        }
    }

    private fun getFavoriteWordsFromDatabase() {
        viewModelScope.launch {
            _favoriteWords.value = dictionaryProviderImpl.getFavoriteWords().map { it.word }
        }
    }

    fun getPreviouslySearchedWordEntry(word: String): WordDTO? {
//        setApiStatus(ApiStatus.Loading())
        _historyApiStatus.value = ApiStatus.Loading()

        var newEntry: WordDTO? = null

        viewModelScope.launch {
            val entry = dictionaryProviderImpl.getPreviouslySearchedWordEntry(word)
            delay(1500)
            Log.i(TAG, "getPreviouslySearchedWordEntry, word is $entry")
            entry?.let { _historyApiStatus.value = ApiStatus.Success(entry) }
            newEntry = entry
        }

        return newEntry
    }

    companion object {
        const val TAG = "HistoryViewModel"
    }
}
