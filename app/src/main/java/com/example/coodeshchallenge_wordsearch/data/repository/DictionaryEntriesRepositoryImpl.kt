package com.example.coodeshchallenge_wordsearch.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.coodeshchallenge_wordsearch.data.sources.local.DictionaryEntryDataSource
import com.example.coodeshchallenge_wordsearch.data.sources.local.dao.DictionaryEntriesDao
import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.DictionaryEntryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class DictionaryEntriesRepositoryImpl(
    private val dictionaryEntriesDao: DictionaryEntriesDao,
    private val dictionaryEntryDataSource: DictionaryEntryDataSource
) : DictionaryEntriesRepository {

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
