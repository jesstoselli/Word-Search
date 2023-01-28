package com.example.coodeshchallenge_wordsearch.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.DictionaryEntryEntity

@Dao
interface DictionaryEntriesDao {

    // Get all saved words from db
    @Query("select * from words")
    fun getWordsList(): List<DictionaryEntryEntity>

    @Query("select count(1) from words")
    fun getWordCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllWords(wordsList: List<DictionaryEntryEntity>)

    // Clear db
    @Query("delete from words")
    fun deleteAll()
}
