package com.example.coodeshchallenge_wordsearch.data.sources.remote.networkmodel

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ListOfWords(
    @Json(name = "data") val data: List<Word>
)

@JsonClass(generateAdapter = true)
data class Word(
    @Json(name = "word") @SerializedName("word") val word: String = "",
    @Json(name = "phonetic") @SerializedName("phonetic") val phonetic: String = "",
    @Json(name = "phonetics") @SerializedName("phonetics") val phonetics: List<Phonetic> = listOf(),
    @Json(name = "origin") @SerializedName("origin") val origin: String = "",
    @Json(name = "meanings") @SerializedName("meanings") val meanings: List<Meaning> = listOf()
)

@JsonClass(generateAdapter = true)
data class Meaning(
    @Json(name = "partOfSpeech") @SerializedName("partOfSpeech") val partOfSpeech: String = "",
    @Json(name = "definitions") @SerializedName("definitions") val definitions: List<Definition> = listOf()
)

@JsonClass(generateAdapter = true)
data class Definition(
    @Json(name = "definition") @SerializedName("definition") val definition: String = "",
    @Json(name = "example") @SerializedName("example") val example: String = "",
    @Json(name = "synonyms") @SerializedName("synonyms") val synonyms: List<String> = listOf(),
    @Json(name = "antonyms") @SerializedName("antonyms") val antonyms: List<String> = listOf(),
)

@JsonClass(generateAdapter = true)
data class Phonetic(
    @Json(name = "text") @SerializedName("text") val text: String = "",
    @Json(name = "audio") @SerializedName("audio") val audio: String? = null
)
