package com.example.coodeshchallenge_wordsearch.data.sources.local.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize

@Entity(tableName = "searched")
@TypeConverters(Converters::class)
@Parcelize
data class WordEntity(
    @PrimaryKey @ColumnInfo(name = "word") val word: String,
    @ColumnInfo(name = "phonetics_text") val phoneticsText: String,
    @ColumnInfo(name = "phonetic_audio") val phoneticsAudio: String,
    @ColumnInfo(name = "meanings") val meanings: List<MeaningEntity>,
    @ColumnInfo(name = "favorite") val favorite: Boolean
) : Parcelable

@Parcelize
data class MeaningEntity(
    @PrimaryKey @ColumnInfo(name = "part_of_speech") val partOfSpeech: String,
    @ColumnInfo(name = "definition") val definition: String,
    @ColumnInfo(name = "example") val example: String,
) : Parcelable
