package com.example.coodeshchallenge_wordsearch.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coodeshchallenge_wordsearch.ui.model.WordDTO
import com.example.coodeshchallenge_wordsearch.utils.ApiStatus

open class BaseViewModel : ViewModel() {

//    private val _apiStatus = MutableLiveData<ApiStatus<WordDTO>>()
//    val apiStatus: LiveData<ApiStatus<WordDTO>>
//        get() = _apiStatus

    private val _isWordFavorite = MutableLiveData<Boolean>(false)
    val isWordFavorite: LiveData<Boolean>
        get() = _isWordFavorite

    private val _navigateToWordPage = MutableLiveData<String?>()
    val navigateToWordPage: LiveData<String?>
        get() = _navigateToWordPage


    //    fun setApiStatus(apiStatus: ApiStatus<WordDTO>) {
//        _apiStatus.postValue(apiStatus)
//    }
//
    fun setFavorite(isFavorite: Boolean) {
        _isWordFavorite.postValue(isFavorite)
    }

    fun navigateToWordPage(word: String) {
        _navigateToWordPage.postValue(word)
    }

    fun returnFromWordPage() {
        _navigateToWordPage.postValue(null)
    }
}

