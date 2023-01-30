package com.example.coodeshchallenge_wordsearch.data.repository

import android.os.RemoteException
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.coodeshchallenge_wordsearch.data.sources.local.DictionaryEntryDataSource
import com.example.coodeshchallenge_wordsearch.data.sources.local.dao.DictionaryEntriesDao
import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.DictionaryEntryEntity
import com.example.coodeshchallenge_wordsearch.data.sources.remote.DictionaryApiService
import com.example.coodeshchallenge_wordsearch.data.sources.remote.networkmodel.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class DictionaryEntriesRepositoryImpl(
    private val dictionaryService: DictionaryApiService,
    private val dictionaryEntriesDao: DictionaryEntriesDao,
    private val dictionaryEntryDataSource: DictionaryEntryDataSource
) : DictionaryEntriesRepository {

    override suspend fun getWordDefinition(word: String): List<Word> {
        try {
            return dictionaryService.getDefinition(word)

        } catch (ex: HttpException) {
            throw RemoteException("Unable to retrieve word definition.")
        }
    }

    override suspend fun getWordsList(): List<DictionaryEntryEntity> {
        return dictionaryEntriesDao.getWordsList()
    }

    override suspend fun getWordsListWithPagination(scope: CoroutineScope): Flow<PagingData<DictionaryEntryEntity>> {
        return Pager(PagingConfig(pageSize = 60)) { dictionaryEntryDataSource }.flow

        //.cachedIn(scope)
    }

    override suspend fun populateDatabaseFromFile(wordsList: List<DictionaryEntryEntity>) {
        dictionaryEntriesDao.insertAllWords(wordsList)
    }

    override suspend fun getWordCount(): Int {
        return dictionaryEntriesDao.getWordCount()
    }
}
