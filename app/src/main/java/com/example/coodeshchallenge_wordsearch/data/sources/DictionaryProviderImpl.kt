package com.example.coodeshchallenge_wordsearch.data.sources

import android.util.Log
import androidx.paging.PagingData
import com.example.coodeshchallenge_wordsearch.data.repository.DictionaryEntriesRepository
import com.example.coodeshchallenge_wordsearch.data.repository.SearchedRepository
import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.DictionaryEntryEntity
import com.example.coodeshchallenge_wordsearch.data.sources.remote.networkmodel.Word
import com.example.coodeshchallenge_wordsearch.ui.model.WordDTO
import com.example.coodeshchallenge_wordsearch.utils.mappers.WordDTOMapper
import com.example.coodeshchallenge_wordsearch.utils.mappers.WordEntityMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class DictionaryProviderImpl(
    private val searchedRepositoryImpl: SearchedRepository,
    private val dictionaryEntriesRepositoryImpl: DictionaryEntriesRepository,
    private val wordEntityMapper: WordEntityMapper,
    private val wordDTOMapper: WordDTOMapper
) : DictionaryProvider {

    override suspend fun getWordsList(): List<String> {
        val wordEntryEntities = dictionaryEntriesRepositoryImpl.getWordsList()
        val wordsList = mutableListOf<String>()
        wordEntryEntities.map { wordEntryEntity -> wordsList.add(wordEntryEntity.word) }

        return wordsList
    }

    override suspend fun getPaginatedWordsList(scope: CoroutineScope): Flow<PagingData<DictionaryEntryEntity>> {
        return dictionaryEntriesRepositoryImpl.getWordsListWithPagination(scope)
    }

    // Favorites
    override suspend fun getFavoriteWords(): List<WordDTO> {
        val dataWordsList = searchedRepositoryImpl.getFavoriteWords()
        return dataWordsList.map { wordDTOMapper.toDomain(it) }
    }

    override suspend fun toggleFavoriteWord(word: String, isFavorite: Boolean) {
        searchedRepositoryImpl.toggleFavoriteWord(word, isFavorite)
    }

    // Search History
    override suspend fun getPreviouslySearchedWords(): List<WordDTO> {
        val dataWordsList = searchedRepositoryImpl.getPreviouslySearchedWords()
        return dataWordsList.map { wordDTOMapper.toDomain(it) }
    }

    override suspend fun getPreviouslySearchedWordEntry(word: String): WordDTO {
        val dataWordsList = searchedRepositoryImpl.getPreviouslySearchedWordEntry(word)

        if (dataWordsList.isEmpty()) return WordDTO()

        return wordDTOMapper.toDomain(dataWordsList.first())
    }

    override suspend fun getRandomWordEntry(): WordDTO {
        val word = dictionaryEntriesRepositoryImpl.getRandomWordDefinition().first()

        val wordEntity = wordEntityMapper.toDomain(word)

        return wordDTOMapper.toDomain(wordEntity)
    }

    override suspend fun getRandomPreviouslySearchedWordEntry(): WordDTO {
        val dataWordsList = searchedRepositoryImpl.getRandomPreviouslySearchedWordEntry()

        if (dataWordsList.isEmpty()) return WordDTO()

        return wordDTOMapper.toDomain(dataWordsList.first())
    }

    override suspend fun addWordToSearchHistory(word: Word) {
        searchedRepositoryImpl.addWordToSearchHistory(wordEntityMapper.toDomain(word))
    }

    override suspend fun removeWordFromSearchHistory(word: String) {
        searchedRepositoryImpl.removeWordFromSearchHistory(word)
    }

    override suspend fun clearSearchHistory() {
        searchedRepositoryImpl.clearSearchHistory()
    }

    // Remote Service
    override suspend fun getWordDefinition(word: String): WordDTO {
        val retrievedWord = dictionaryEntriesRepositoryImpl.getWordDefinition(word).first()
//        val retrievedWord = dictionaryEntriesRepositoryImpl.wordDefinitionFromAPI.value!!.first()

        Log.i("DictionaryProvider", retrievedWord.word)

        addWordToSearchHistory(retrievedWord)

        val wordEntity = wordEntityMapper.toDomain(retrievedWord)

        return wordDTOMapper.toDomain(wordEntity)
    }

    // Setting up database
    override suspend fun populateDatabaseFromFile(listOfWords: List<String>) {

        val listOfDictionaryEntries = mutableListOf<DictionaryEntryEntity>()
        listOfWords.map {
            listOfDictionaryEntries.add(
                DictionaryEntryEntity(word = it)
            )
        }
        dictionaryEntriesRepositoryImpl.populateDatabaseFromFile(listOfDictionaryEntries)
    }

    override suspend fun getWordCount(): Int {
        return dictionaryEntriesRepositoryImpl.getWordCount()
    }
}
