package com.example.coodeshchallenge_wordsearch.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    private val _navigateToWordPage = MutableLiveData<String?>()
    val navigateToWordPage: LiveData<String?>
        get() = _navigateToWordPage

    fun navigateToWordPage(word: String) {
        _navigateToWordPage.postValue(word)
    }

    fun returnFromWordPage() {
        _navigateToWordPage.postValue(null)
    }
}

