package com.example.coodeshchallenge_wordsearch.data.sources.remote.networkmodel

data class Word(
    val word: String = "",
    val phonetic: String  = "",
    val phonetics: List<Phonetic> = listOf(),
    val origin: String = "",
    val meanings: List<Meaning> = listOf()
)

data class Meaning (
    val partOfSpeech: String = "",
    val definitions: List<Definition> = listOf()
)

data class Definition (
    val definition: String = "",
    val example: String = "",
    val synonyms: List<String> = listOf(),
    val antonyms: List<String> = listOf(),
)

data class Phonetic (
    val text: String = "",
    val audio: String? = null
)
