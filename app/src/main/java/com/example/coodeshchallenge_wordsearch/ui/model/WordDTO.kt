package com.example.coodeshchallenge_wordsearch.ui.model

data class WordDTO(
    val word: String = "",
    val phoneticsText: String = "",
    val phoneticsAudio: String? = null,
    val meanings: List<MeaningDTO> = listOf(),
    val favorite: Boolean = false
)

data class MeaningDTO(
    val partOfSpeech: String = "",
    val definition: String = "",
    val example: String = ""
)
