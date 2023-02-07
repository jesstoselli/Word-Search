package com.example.coodeshchallenge_wordsearch.ui.wordpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.coodeshchallenge_wordsearch.data.sources.DictionaryProvider
import com.example.coodeshchallenge_wordsearch.data.sources.remote.networkmodel.ApiError
import com.example.coodeshchallenge_wordsearch.ui.BaseViewModel
import com.example.coodeshchallenge_wordsearch.ui.model.WordDTO
import com.example.coodeshchallenge_wordsearch.utils.ApiStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WordPageViewModel(private val dictionaryProviderImpl: DictionaryProvider) : BaseViewModel() {

    private val _apiStatus = MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus

    private val _isWordFavorite = MutableLiveData<Boolean>()
    val isWordFavorite: LiveData<Boolean>
        get() = _isWordFavorite

    private val _selectedWord = MutableLiveData<WordDTO?>()
    val selectedWord: LiveData<WordDTO?>
        get() = _selectedWord

    private val _errorMessage = MutableLiveData<ApiError>()
    val apiErrorMessage: LiveData<ApiError>
        get() = _errorMessage


    fun setErrorMessage(apiError: ApiError) {
        _errorMessage.postValue(apiError)
    }

    fun setFavorite(isFavorite: Boolean) {
        _isWordFavorite.postValue(isFavorite)
    }

    fun setApiStatus(apiStatus: ApiStatus) {
        _apiStatus.postValue(apiStatus)
    }

    fun toggleFavoriteWord(word: String, isFavorite: Boolean) {
        viewModelScope.launch {
            dictionaryProviderImpl.toggleFavoriteWord(word, isFavorite)
        }
    }

    fun getRandomWordDefinition() {
        viewModelScope.launch {
            val word = dictionaryProviderImpl.getRandomWordEntry()
            _selectedWord.postValue(word)
            _apiStatus.postValue(ApiStatus.SUCCESS)
        }
    }

    fun getRandomPreviouslySearchedWordEntry() {
        viewModelScope.launch {
            val word = dictionaryProviderImpl.getRandomPreviouslySearchedWordEntry()
            _selectedWord.postValue(word)
            _apiStatus.postValue(ApiStatus.SUCCESS)
        }
    }

    fun getWordDefinition(requestedWord: String) {
        _apiStatus.postValue(ApiStatus.LOADING)

        viewModelScope.launch {

            val entry = dictionaryProviderImpl.getPreviouslySearchedWordEntry(requestedWord)
            delay(1500)
            Log.i(TAG, "getPreviouslySearchedWordEntry, word is $entry")

            if (entry.word.isNotEmpty()) {
                _selectedWord.postValue(entry)
                _apiStatus.postValue(ApiStatus.SUCCESS)
            } else {

                try {
                    val retrievedWordDefinition = dictionaryProviderImpl.getWordDefinition(requestedWord)
                    delay(2000L)
                    _selectedWord.postValue(retrievedWordDefinition)
                    _apiStatus.postValue(ApiStatus.SUCCESS)

                } catch (e: Exception) {
                    val message =
                        if (e.localizedMessage.isNullOrEmpty()) "Something went wrong while loading data." else e.localizedMessage
                    Log.e(TAG, message)
                    _apiStatus.postValue(ApiStatus.ERROR)
                }
            }
        }
    }

    companion object {
        const val TAG = "WordPageViewModel"
    }
}
