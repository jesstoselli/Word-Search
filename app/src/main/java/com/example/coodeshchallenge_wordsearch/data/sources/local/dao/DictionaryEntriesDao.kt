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

//    @Query("SELECT * FROM words WHERE word IN (SELECT word FROM words ORDER BY RANDOM() LIMIT :size) ")
//    suspend fun getRandomWordItems(size: Int): List<DictionaryEntryEntity>

    @Query("SELECT * FROM words LIMIT :limit OFFSET :offset")
    fun getWordItems(limit: Int, offset: Int): List<DictionaryEntryEntity>

    @Query("select count(1) from words")
    fun getWordCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllWords(wordsList: List<DictionaryEntryEntity>)

    // Clear db
    @Query("delete from words")
    fun deleteAll()
}
