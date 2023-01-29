package com.example.coodeshchallenge_wordsearch.utils

import java.util.*

fun String.toFirstCapitalLetters(word: String): String {
    return word.substring(0, 1).uppercase(Locale.getDefault()) + word.substring(1)
}
