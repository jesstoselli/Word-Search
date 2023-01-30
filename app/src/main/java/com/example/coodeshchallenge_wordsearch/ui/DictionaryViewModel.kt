package com.example.coodeshchallenge_wordsearch.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.coodeshchallenge_wordsearch.data.sources.DictionaryProvider
import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.DictionaryEntryEntity
import com.example.coodeshchallenge_wordsearch.ui.model.WordDTO
import com.example.coodeshchallenge_wordsearch.utils.ApiStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.IOException

class DictionaryViewModel(
    private val dictionaryProviderImpl: DictionaryProvider
) : ViewModel() {

    private val _dictionaryApiStatus = MutableLiveData<ApiStatus<WordDTO>>()
    val dictionaryApiStatus: LiveData<ApiStatus<WordDTO>>
        get() = _dictionaryApiStatus

    private var _wordsFlow: Flow<PagingData<DictionaryEntryEntity>> = flow { }

    val wordsFlow: Flow<PagingData<DictionaryEntryEntity>>
        get() = _wordsFlow

    private val _navigateToWordPage = MutableLiveData<String?>()
    val navigateToWordPage: LiveData<String?>
        get() = _navigateToWordPage

    init {
        Log.d(TAG, "Loaded properly")
        getWordsFromDatabase()
    }

    fun getWordsFromDatabase() {
        viewModelScope.launch {
            _wordsFlow =
                dictionaryProviderImpl.getPaginatedWordsList(viewModelScope).cachedIn(viewModelScope)
                    .catch { exception ->
                        Log.e(TAG, exception.toString())
                    }
        }
    }

    fun navigateToWordPage(word: String) {
        _dictionaryApiStatus.value = ApiStatus.Loading()
        _navigateToWordPage.value = word
    }

    fun returnFromWordPage() {
        _navigateToWordPage.value = null
    }

    fun getWordDefinitionFromAPI(requestedWord: String) {
        _dictionaryApiStatus.value = ApiStatus.Loading()

        viewModelScope.launch {
            val hasBeenSearched = getPreviouslySearchedWordEntry(requestedWord)

            if (hasBeenSearched == null || hasBeenSearched.word.isEmpty()) {
                try {
                    val retrievedWordDefinition = dictionaryProviderImpl.getWordDefinition(requestedWord)
                    _dictionaryApiStatus.value = ApiStatus.Success(retrievedWordDefinition)
                } catch (e: Exception) {
                    val message =
                        if (e.localizedMessage.isNullOrEmpty()) "Something went wrong while loading data." else e.localizedMessage
                    Log.e(TAG, message)
                    _dictionaryApiStatus.value = ApiStatus.Error(message, null)
                }
            } else {
                _dictionaryApiStatus.value = ApiStatus.Success(hasBeenSearched)
            }
        }
    }

    private fun getPreviouslySearchedWordEntry(word: String): WordDTO? {
        var newEntry: WordDTO? = null

        viewModelScope.launch {
            val entry = dictionaryProviderImpl.getPreviouslySearchedWordEntry(word)
            Log.i(TAG, "getPreviouslySearchedWordEntry")
            Log.i(TAG, entry.toString())
            _dictionaryApiStatus.value = ApiStatus.Success(entry)
            newEntry = entry
        }

        return newEntry
    }

    fun populateDatabaseFromFile(context: Context, fileName: String) {

        viewModelScope.launch {

            if (dictionaryProviderImpl.getWordCount() == 0) {

                val wordsListFromTxt = mutableListOf<String>()

                try {
                    context.assets.open(fileName).bufferedReader().use { br ->
                        var line: String?
                        val thresholdToSendToDB = 15000

                        while (br.readLine().also { line = it } != null) {
                            wordsListFromTxt.add(line!!)

                            if (wordsListFromTxt.count() > thresholdToSendToDB) {
                                dictionaryProviderImpl.populateDatabaseFromFile(wordsListFromTxt)
                                wordsListFromTxt.clear()
                            }
                        }

                        if (wordsListFromTxt.isNotEmpty()) {
                            dictionaryProviderImpl.populateDatabaseFromFile(wordsListFromTxt)
                        }

                        br.close()
                    }
                } catch (ioException: IOException) {
                    ioException.printStackTrace()
                }
            }
        }
    }

    companion object {
        const val TAG = "DictionaryViewModel"
    }
}


//    private val listOfWords = listOf(
//        "avocado",
//        "banana",
//        "papaya",
//        "kiwi",
//        "fig",
//        "orange",
//        "lemon",
//        "blackberry",
//        "tomato",
//        "raspberry",
//        "blueberry",
//        "pitaya",
//        "avocado",
//        "banana",
//        "papaya",
//        "kiwi",
//        "fig",
//        "orange",
//        "lemon",
//        "blackberry",
//        "tomato",
//        "raspberry",
//        "blueberry",
//        "pitaya",
//        "avocado",
//        "banana",
//        "papaya",
//        "kiwi",
//        "fig",
//        "orange",
//        "lemon",
//        "blackberry",
//        "tomato",
//        "raspberry",
//        "blueberry",
//        "pitaya",
//        "avocado",
//        "banana",
//        "papaya",
//        "kiwi",
//        "fig",
//        "orange",
//        "lemon",
//        "blackberry",
//        "tomato",
//        "raspberry",
//        "blueberry",
//        "pitaya"
//    )
