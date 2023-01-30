package com.example.coodeshchallenge_wordsearch.data.sources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.WordEntity

@Dao
interface SearchedDao {

    @Query("select * from searched WHERE favorite = 1")
    fun getFavoriteWords(): List<WordEntity>

    // Favorite word
    @Query("update searched SET favorite = :isFavorite WHERE word LIKE :word")
    suspend fun toggleFavoriteWord(word: String, isFavorite: Boolean)

    @Query("select * from searched")
    fun getPreviouslySearchedWords(): List<WordEntity>

    @Query("select * from searched WHERE word LIKE :word")
    fun getPreviouslySearchedWordEntry(word: String): List<WordEntity>

    @Query("SELECT * FROM searched ORDER BY RANDOM() LIMIT 1")
    fun getRandomPreviouslySearchedWordEntry(): List<WordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWordToSearchHistory(wordEntity: WordEntity)

    //Delete single entry query
    @Query("DELETE FROM searched where word LIKE :word")
    suspend fun removeWordFromSearchHistory(word: String)

    // Clear db
    @Query("delete from searched")
    fun deleteAll()
}
