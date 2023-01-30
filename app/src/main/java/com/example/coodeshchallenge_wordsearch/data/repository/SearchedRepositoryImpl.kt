package com.example.coodeshchallenge_wordsearch.data.repository

import com.example.coodeshchallenge_wordsearch.data.sources.local.dao.SearchedDao
import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.WordEntity
import com.example.coodeshchallenge_wordsearch.utils.wrapEspressoIdlingResource

class SearchedRepositoryImpl(private val searchedDao: SearchedDao) : SearchedRepository {

    override suspend fun getPreviouslySearchedWords(): List<WordEntity> {
        wrapEspressoIdlingResource {
            return searchedDao.getPreviouslySearchedWords()
        }
    }

    override suspend fun getPreviouslySearchedWordEntry(word: String): List<WordEntity> {
        wrapEspressoIdlingResource {
            return searchedDao.getPreviouslySearchedWordEntry(word)
        }
    }

    override suspend fun getRandomPreviouslySearchedWordEntry(): List<WordEntity> {
        wrapEspressoIdlingResource {
            return searchedDao.getRandomPreviouslySearchedWordEntry()
        }
    }

    override suspend fun addWordToSearchHistory(wordEntity: WordEntity) {
        wrapEspressoIdlingResource {
            return searchedDao.addWordToSearchHistory(wordEntity)
        }
    }

    override suspend fun removeWordFromSearchHistory(word: String) {
        wrapEspressoIdlingResource {
            return searchedDao.removeWordFromSearchHistory(word)
        }
    }

    override suspend fun clearSearchHistory() {
        wrapEspressoIdlingResource {
            return searchedDao.deleteAll()
        }
    }

    override suspend fun getFavoriteWords(): List<WordEntity> {
        wrapEspressoIdlingResource {
            return searchedDao.getFavoriteWords()
        }
    }

    override suspend fun toggleFavoriteWord(word: String, isFavorite: Boolean) {
        wrapEspressoIdlingResource {
            return searchedDao.toggleFavoriteWord(word, isFavorite)
        }
    }

    // Clear database
    override suspend fun deleteAll() {
        wrapEspressoIdlingResource {
            return searchedDao.deleteAll()
        }
    }
}
