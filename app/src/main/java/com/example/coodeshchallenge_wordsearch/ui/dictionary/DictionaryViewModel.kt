package com.example.coodeshchallenge_wordsearch.ui.dictionary

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.coodeshchallenge_wordsearch.data.sources.DictionaryProvider
import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.DictionaryEntryEntity
import com.example.coodeshchallenge_wordsearch.ui.BaseViewModel
import com.example.coodeshchallenge_wordsearch.ui.model.WordDTO
import com.example.coodeshchallenge_wordsearch.utils.ApiStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.IOException

class DictionaryViewModel(
    private val dictionaryProviderImpl: DictionaryProvider
) : BaseViewModel() {

//    private val _dictionaryApiStatus = MutableLiveData<ApiStatus>()
//    val dictionaryApiStatus: LiveData<ApiStatus>
//        get() = _dictionaryApiStatus

    private var _wordsFlow: Flow<PagingData<DictionaryEntryEntity>> = flow { }

    val wordsFlow: Flow<PagingData<DictionaryEntryEntity>>
        get() = _wordsFlow

    init {
        Log.d(TAG, "Loaded properly")
        getWordsFromDatabase()
    }

//    fun setApiStatus(apiStatus: ApiStatus) {
//        _dictionaryApiStatus.postValue(apiStatus)
//    }

    fun getWordsFromDatabase() {
        viewModelScope.launch {
            _wordsFlow =
                dictionaryProviderImpl.getPaginatedWordsList(viewModelScope).cachedIn(viewModelScope)
                    .catch { exception ->
                        Log.e(TAG, exception.toString())
                    }
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
            }
        }
    }

    companion object {
        const val TAG = "DictionaryViewModel"
    }
}
