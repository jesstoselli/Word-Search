package com.example.coodeshchallenge_wordsearch.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.DictionaryEntryEntity
import com.example.coodeshchallenge_wordsearch.data.sources.remote.networkmodel.ApiError
import com.example.coodeshchallenge_wordsearch.data.sources.remote.networkmodel.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface DictionaryEntriesRepository {

//    val wordDefinitionFromAPI: LiveData<List<Word>>

//    val errorMessage: LiveData<ApiError>

//    suspend fun getWordDefinition(word: String)

//    suspend fun getRandomWordDefinition()

    suspend fun getRandomDictionaryEntry(): DictionaryEntryEntity

    suspend fun getWordsList(): List<DictionaryEntryEntity>

    suspend fun getWordsListWithPagination(scope: CoroutineScope): Flow<PagingData<DictionaryEntryEntity>>

    suspend fun populateDatabaseFromFile(wordsList: List<DictionaryEntryEntity>)

    suspend fun getWordCount(): Int
}
