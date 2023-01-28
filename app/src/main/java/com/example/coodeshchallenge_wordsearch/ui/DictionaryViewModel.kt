package com.example.coodeshchallenge_wordsearch.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coodeshchallenge_wordsearch.data.sources.DictionaryProvider
import com.example.coodeshchallenge_wordsearch.ui.model.WordDTO
import com.example.coodeshchallenge_wordsearch.utils.ApiStatus
import kotlinx.coroutines.launch
import java.io.IOException

class DictionaryViewModel(private val dictionaryProviderImpl: DictionaryProvider) : ViewModel() {

    private val _apiStatus = MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus

    private val _favoriteWords = MutableLiveData<List<String>>()
    val favoriteWords: LiveData<List<String>>
        get() = _favoriteWords

    private val _previouslySearchedWords = MutableLiveData<List<String>>()
    val previouslySearchedWords: LiveData<List<String>>
        get() = _previouslySearchedWords

    private val _wordsList = MutableLiveData<List<String>>()
    val wordsList: LiveData<List<String>>
        get() = _wordsList

    private val _chosenWord = MutableLiveData<WordDTO>()
    val chosenWord: LiveData<WordDTO>
        get() = _chosenWord

    private val _recyclerViewData = MutableLiveData<List<String>>()
    val recyclerViewData: LiveData<List<String>>
        get() = _recyclerViewData

    private val _navigateToWordPage = MutableLiveData<String?>()
    val navigateToWordPage: LiveData<String?>
        get() = _navigateToWordPage


    val listOfWords = listOf(
        "avocado",
        "banana",
        "papaya",
        "kiwi",
        "fig",
        "orange",
        "lemon",
        "blackberry",
        "tomato",
        "raspberry",
        "blueberry",
        "pitaya",
        "avocado",
        "banana",
        "papaya",
        "kiwi",
        "fig",
        "orange",
        "lemon",
        "blackberry",
        "tomato",
        "raspberry",
        "blueberry",
        "pitaya",
        "avocado",
        "banana",
        "papaya",
        "kiwi",
        "fig",
        "orange",
        "lemon",
        "blackberry",
        "tomato",
        "raspberry",
        "blueberry",
        "pitaya",
        "avocado",
        "banana",
        "papaya",
        "kiwi",
        "fig",
        "orange",
        "lemon",
        "blackberry",
        "tomato",
        "raspberry",
        "blueberry",
        "pitaya"
    )

    init {
        Log.d(TAG, "Loaded properly")
        _wordsList.value = listOfWords
//        getWordsFromDatabase()
//        getFavoriteWordsFromDatabase()
//        getPreviouslySearchedWords()
    }

    fun chosenWordList(section: String) {
        viewModelScope.launch {
            when (section) {
                "allWords" -> _recyclerViewData.value = wordsList.value
                "favorites" -> {
                    getFavoriteWordsFromDatabase()
                    _recyclerViewData.value = favoriteWords.value
                }
                "history" -> {
                    getPreviouslySearchedWords()
                    _recyclerViewData.value = previouslySearchedWords.value
                }
            }
        }
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

    private fun getWordDefinitionFromAPI(requestedWord: String) {
        _apiStatus.value = ApiStatus.LOADING

        viewModelScope.launch {
            try {
                val retrievedWordDefinition = dictionaryProviderImpl.getWordDefinition(requestedWord)
                _apiStatus.value = ApiStatus.DONE
                _chosenWord.value = retrievedWordDefinition
            } catch (e: Exception) {
                val message =
                    if (e.localizedMessage.isNullOrEmpty()) "Something went wrong while loading data." else e.localizedMessage
                Log.e(TAG, message)
                _apiStatus.value = ApiStatus.ERROR
                _chosenWord.value = WordDTO()
            }
        }

//        var requestedWordEntity = Word()

//        viewModelScope.launch {
//            val hasBeenSearched = getPreviouslySearchedWordEntry(requestedWord)
//
//            if (hasBeenSearched == null) {
//                val retrievedWordDefinition = dictionaryProviderImpl.getWordDefinition(requestedWord)
//                _chosenWord.value = retrievedWordDefinition
////                requestedWordEntity = retrievedWordDefinition
//            } else if (hasBeenSearched.word.isNotEmpty()) {
//                _chosenWord.value = hasBeenSearched
////                requestedWordEntity = hasBeenSearched
//            }
//        }

//        return requestedWordEntity
    }

    fun navigateToWordPage(word: String) {
        getWordDefinitionFromAPI(word)
        _navigateToWordPage.value = word
    }

    fun returnFromWordPage() {
        _navigateToWordPage.value = null
    }

    private fun getPreviouslySearchedWordEntry(word: String): WordDTO? {
        var newEntry: WordDTO? = null

        viewModelScope.launch {
            val entry = dictionaryProviderImpl.getPreviouslySearchedWordEntry(word)
            _chosenWord.value = entry
            newEntry = entry
        }

        return newEntry
    }

    private fun getFavoriteWordsFromDatabase() {
        viewModelScope.launch {
            _favoriteWords.value = dictionaryProviderImpl.getFavoriteWords().map { it.word }
        }
    }

    private fun getPreviouslySearchedWords() {
        viewModelScope.launch {
            _previouslySearchedWords.value = dictionaryProviderImpl.getPreviouslySearchedWords().map { it.word }
        }
    }

    private fun getWordsFromDatabase() {
        viewModelScope.launch {
            _wordsList.value = dictionaryProviderImpl.getWordsList()
        }
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


                _wordsList.value = wordsListFromTxt
                _recyclerViewData.value = wordsList.value
            } else {
                _wordsList.value = dictionaryProviderImpl.getWordsList()
                _recyclerViewData.value = wordsList.value
            }
        }
    }

    companion object {
        const val TAG = "DictionaryViewModel"
    }
}
