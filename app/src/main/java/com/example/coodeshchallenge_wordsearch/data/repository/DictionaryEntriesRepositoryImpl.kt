package com.example.coodeshchallenge_wordsearch.data.repository

import android.os.RemoteException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.coodeshchallenge_wordsearch.data.sources.local.DictionaryEntryDataSource
import com.example.coodeshchallenge_wordsearch.data.sources.local.dao.DictionaryEntriesDao
import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.DictionaryEntryEntity
import com.example.coodeshchallenge_wordsearch.data.sources.remote.DictionaryApi
import com.example.coodeshchallenge_wordsearch.data.sources.remote.DictionaryApi.dictionaryService
import com.example.coodeshchallenge_wordsearch.data.sources.remote.networkmodel.ApiError
import com.example.coodeshchallenge_wordsearch.data.sources.remote.networkmodel.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class DictionaryEntriesRepositoryImpl(
    private val dictionaryEntriesDao: DictionaryEntriesDao,
    private val dictionaryEntryDataSource: DictionaryEntryDataSource
) : DictionaryEntriesRepository {

//    private val _errorMessage = MutableLiveData<ApiError>()
//    override val errorMessage: LiveData<ApiError>
//        get() = _errorMessage

//    private val _wordDefinitionFromAPI = MutableLiveData<List<Word>>()
//    override val wordDefinitionFromAPI: LiveData<List<Word>>
//        get() = _wordDefinitionFromAPI

//    override suspend fun getWordDefinition(word: String) {
//
//        try {
//            CoroutineScope(Dispatchers.IO).launch {
//
//                val response = dictionaryService.getDefinition(word)
//
////                withContext(Dispatchers.Main) {
//
//                try {
//                    if (response.isSuccessful) {
//                        Log.d("DictionaryEntriesRepositoryImpl", response.body().toString())
//                        _wordDefinitionFromAPI.postValue(response.body())
//                    } else {
//                        Log.d("DictionaryEntriesRepositoryImpl", "Error: ${response.code()}")
//                    }
//                } catch (e: HttpException) {
//                    Log.d("DictionaryEntriesRepositoryImpl", "Exception ${e.message}")
//                } catch (e: Throwable) {
//                    Log.d(
//                        "DictionaryEntriesRepositoryImpl", e.message.toString()
//                    )
//                }
////                }
//
//            }
//
//        } catch (ex: HttpException) {
//            throw RemoteException("Unable to retrieve word definition.")
//        }
//    }

//    override suspend fun getRandomWordDefinition() {
//        val entry = dictionaryEntriesDao.getRandomWordEntry().first()
//
//        getWordDefinition(entry.word)
//        return getWordDefinition(entry.word)
//    }

    override suspend fun getRandomDictionaryEntry(): DictionaryEntryEntity {
        return dictionaryEntriesDao.getRandomWordEntry().first()
    }

    override suspend fun getWordsList(): List<DictionaryEntryEntity> {
        return dictionaryEntriesDao.getWordsList()
    }

    override suspend fun getWordsListWithPagination(scope: CoroutineScope): Flow<PagingData<DictionaryEntryEntity>> {
        return Pager(PagingConfig(pageSize = 60)) { dictionaryEntryDataSource }.flow
    }

    override suspend fun populateDatabaseFromFile(wordsList: List<DictionaryEntryEntity>) {
        dictionaryEntriesDao.insertAllWords(wordsList)
    }

    override suspend fun getWordCount(): Int {
        return dictionaryEntriesDao.getWordCount()
    }
}
