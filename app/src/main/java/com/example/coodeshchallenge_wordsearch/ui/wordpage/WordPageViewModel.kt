package com.example.coodeshchallenge_wordsearch.ui.wordpage

import android.os.RemoteException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.coodeshchallenge_wordsearch.data.sources.DictionaryProvider
import com.example.coodeshchallenge_wordsearch.data.sources.remote.DictionaryApi
import com.example.coodeshchallenge_wordsearch.data.sources.remote.networkmodel.Word
import com.example.coodeshchallenge_wordsearch.ui.BaseViewModel
import com.example.coodeshchallenge_wordsearch.ui.model.WordDTO
import com.example.coodeshchallenge_wordsearch.utils.ApiStatus
import com.example.coodeshchallenge_wordsearch.utils.mappers.WordToWordDTOMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException


class WordPageViewModel(
    private val dictionaryProviderImpl: DictionaryProvider,
    private val wordToWordDTOMapper: WordToWordDTOMapper
) : BaseViewModel() {

    private val _apiStatus = MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus

    private val _isWordFavorite = MutableLiveData<Boolean>()
    val isWordFavorite: LiveData<Boolean>
        get() = _isWordFavorite

    private val _selectedWord = MutableLiveData<WordDTO?>()
    val selectedWord: LiveData<WordDTO?>
        get() = _selectedWord

    var apiErrorMessage: String = ""

    private val _wordDefinitionFromAPI = MutableLiveData<List<Word>>()


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

    fun getRandomPreviouslySearchedWordEntry() {
        _apiStatus.postValue(ApiStatus.LOADING)

        viewModelScope.launch {
            val word = dictionaryProviderImpl.getRandomPreviouslySearchedWordEntry()
            _selectedWord.postValue(word)

            delay(800L)
            _apiStatus.postValue(ApiStatus.SUCCESS)
        }
    }

    fun getRandomWordDefinition() {

        viewModelScope.launch {
            _apiStatus.postValue(ApiStatus.LOADING)

            val wordEntry = dictionaryProviderImpl.getRandomDictionaryEntry()
            delay(2000L)

            getWordDefinitionFromApi(wordEntry.word)

            selectedWord.value?.let { dictionaryProviderImpl.addWordToSearchHistory(it) }

            delay(2000L)
            _apiStatus.postValue(ApiStatus.SUCCESS)
        }

    }

    fun getWordDefinition(requestedWord: String) {
        _apiStatus.postValue(ApiStatus.LOADING)

        viewModelScope.launch {

            val entry = dictionaryProviderImpl.getPreviouslySearchedWordEntry(requestedWord)
            delay(1500L)
            Log.i(TAG, "getPreviouslySearchedWordEntry, word is $entry")

            if (entry.word.isNotEmpty()) {
                _selectedWord.postValue(entry)
                _apiStatus.postValue(ApiStatus.SUCCESS)

            } else {

                try {
                    getWordDefinitionFromApi(requestedWord)

                } catch (e: Exception) {
                    val message =
                        if (e.localizedMessage.isNullOrEmpty()) "Something went wrong while loading data." else e.localizedMessage
                    Log.e(TAG, message)
                    _apiStatus.postValue(ApiStatus.ERROR)
                }
            }
        }
    }

    private suspend fun getWordDefinitionFromApi(requestedWord: String) {
        try {
            CoroutineScope(Dispatchers.IO).launch {

                val response = DictionaryApi.dictionaryService.getDefinition(requestedWord)

                try {
                    if (response.isSuccessful) {
                        Log.d(TAG, response.body().toString())
                        _wordDefinitionFromAPI.postValue(response.body())

                        val retrievedWordDefinition = response.body()!!.map { wordToWordDTOMapper.toDomain(it) }

                        _selectedWord.postValue(retrievedWordDefinition.first())

                        delay(2000L)
                        _apiStatus.postValue(ApiStatus.SUCCESS)
                    } else {
                        if (response.code() == 404) {
//                            apiErrorMessage = response.message()

                            apiErrorMessage = "404"

                            Log.d(TAG, "Error Body: " + response.body().toString())

                            _apiStatus.postValue(ApiStatus.ERROR)
                        }
                        Log.d(TAG, "Error: ${response.code()}")
                    }
                } catch (e: HttpException) {
                    Log.d(TAG, "Exception ${e.message}")

                    apiErrorMessage = e.message()

                    delay(2000L)
                    _apiStatus.postValue(ApiStatus.ERROR)
                } catch (e: Throwable) {

                    delay(2000L)
                    _apiStatus.postValue(ApiStatus.ERROR)
                    Log.d(TAG, e.message.toString())
                }
            }

        } catch (ex: HttpException) {
            throw RemoteException("Unable to retrieve word definition.")
        }
    }

    companion object {
        const val TAG = "WordPageViewModel"
    }
}
