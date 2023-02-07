package com.example.coodeshchallenge_wordsearch.data.sources

import androidx.paging.PagingData
import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.DictionaryEntryEntity
import com.example.coodeshchallenge_wordsearch.data.sources.remote.networkmodel.Word
import com.example.coodeshchallenge_wordsearch.ui.model.WordDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface DictionaryProvider {

    suspend fun getWordsList(): List<String>

    suspend fun getPaginatedWordsList(scope: CoroutineScope): Flow<PagingData<DictionaryEntryEntity>>

    suspend fun getRandomWordEntry(): WordDTO

    suspend fun getFavoriteWords(): List<WordDTO>

    suspend fun toggleFavoriteWord(word: String, isFavorite: Boolean)

    suspend fun getPreviouslySearchedWords(): List<WordDTO>

    suspend fun getPreviouslySearchedWordEntry(word: String): WordDTO?

    suspend fun getRandomPreviouslySearchedWordEntry(): WordDTO?

    suspend fun addWordToSearchHistory(word: Word)

    suspend fun removeWordFromSearchHistory(word: String)

    suspend fun clearSearchHistory()

    suspend fun getWordDefinition(word: String): WordDTO

    suspend fun populateDatabaseFromFile(listOfWords: List<String>)

    suspend fun getWordCount(): Int
}
