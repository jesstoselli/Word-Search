package com.example.coodeshchallenge_wordsearch.data.sources.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.coodeshchallenge_wordsearch.data.sources.local.dao.DictionaryEntriesDao
import com.example.coodeshchallenge_wordsearch.data.sources.local.dao.SearchedDao
import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.Converters
import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.DictionaryEntryEntity
import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.WordEntity

@Database(entities = [WordEntity::class, DictionaryEntryEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DictionaryDatabase : RoomDatabase() {
    abstract val searchedDao: SearchedDao
    abstract val dictionaryEntriesDao: DictionaryEntriesDao

    companion object {
        private lateinit var INSTANCE: DictionaryDatabase

        fun getDatabase(context: Context): DictionaryDatabase {
            synchronized(DictionaryDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DictionaryDatabase::class.java,
                        "dictionary"
                    ).allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }
    }
}
