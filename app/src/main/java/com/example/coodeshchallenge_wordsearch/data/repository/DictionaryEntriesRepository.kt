package com.example.coodeshchallenge_wordsearch.data.repository

import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.DictionaryEntryEntity
import com.example.coodeshchallenge_wordsearch.data.sources.remote.networkmodel.Word

interface DictionaryEntriesRepository {

    suspend fun getWordDefinition(word: String): List<Word>

    suspend fun getWordsList(): List<DictionaryEntryEntity>

    suspend fun populateDatabaseFromFile(wordsList: List<DictionaryEntryEntity>)

    suspend fun getWordCount() : Int
}
