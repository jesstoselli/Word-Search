package com.example.coodeshchallenge_wordsearch.data.sources.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "words")
@Parcelize
data class DictionaryEntryEntity(

    @PrimaryKey
    val word: String

) : Parcelable
