package com.example.coodeshchallenge_wordsearch.data.repository

import androidx.paging.PagingData
import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.DictionaryEntryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface DictionaryEntriesRepository {

    suspend fun getRandomDictionaryEntry(): DictionaryEntryEntity

    suspend fun getWordsList(): List<DictionaryEntryEntity>

    suspend fun getWordsListWithPagination(scope: CoroutineScope): Flow<PagingData<DictionaryEntryEntity>>

    suspend fun populateDatabaseFromFile(wordsList: List<DictionaryEntryEntity>)

    suspend fun getWordCount(): Int
}
