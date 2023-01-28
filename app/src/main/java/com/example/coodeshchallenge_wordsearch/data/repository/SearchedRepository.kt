package com.example.coodeshchallenge_wordsearch.data.repository

import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.WordEntity

interface SearchedRepository {

    suspend fun getPreviouslySearchedWords(): List<WordEntity>

    suspend fun getPreviouslySearchedWordEntry(word: String): List<WordEntity>

    suspend fun addWordToSearchHistory(wordEntity: WordEntity)

    suspend fun removeWordFromSearchHistory(word: String)

    suspend fun clearSearchHistory()

    suspend fun getFavoriteWords(): List<WordEntity>

    suspend fun toggleFavoriteWord(word: String, isFavorite: Boolean)

    suspend fun deleteAll()
}
