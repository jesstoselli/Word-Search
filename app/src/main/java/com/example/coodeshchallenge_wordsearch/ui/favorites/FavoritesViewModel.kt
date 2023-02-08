package com.example.coodeshchallenge_wordsearch.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.coodeshchallenge_wordsearch.data.sources.DictionaryProvider
import com.example.coodeshchallenge_wordsearch.ui.BaseViewModel
import kotlinx.coroutines.launch

class FavoritesViewModel(private val dictionaryProviderImpl: DictionaryProvider) : BaseViewModel() {

    private val _favoriteWords = MutableLiveData<List<String>>()
    val favoriteWords: LiveData<List<String>>
        get() = _favoriteWords

    init {
        getFavoriteWordsFromDatabase()
    }


    fun getFavoriteWordsFromDatabase() {
        viewModelScope.launch {
            _favoriteWords.value = dictionaryProviderImpl.getFavoriteWords().map { it.word }
        }
    }

}
