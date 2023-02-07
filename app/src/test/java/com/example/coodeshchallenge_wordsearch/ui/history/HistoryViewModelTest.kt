package com.example.coodeshchallenge_wordsearch.ui.history

import com.example.coodeshchallenge_wordsearch.MainDispatcherRule
import com.example.coodeshchallenge_wordsearch.data.sources.DictionaryProvider
import com.example.coodeshchallenge_wordsearch.ui.model.MeaningDTO
import com.example.coodeshchallenge_wordsearch.ui.model.WordDTO
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

//@RunWith(AndroidJUnit4::class)
class HistoryViewModelTest {

    private val provider: DictionaryProvider = mockk(relaxed = true)

    lateinit var viewModel: HistoryViewModel

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        viewModel = HistoryViewModel(provider)
    }

    val banana = WordDTO(
        word = "Banana",
        phoneticsText = "bəˈnɑːnə",
        phoneticsAudio = null,
        meanings = listOf(
            MeaningDTO(
                partOfSpeech = "Substantive",
                definition = "Yellow fruit",
                example = "I'm eating a banana."
            )
        ),
        favorite = false
    )

    val fig = WordDTO(
        word = "Fig",
        phoneticsText = "fɪɡ",
        phoneticsAudio = null,
        meanings = listOf(
            MeaningDTO(
                partOfSpeech = "Substantive",
                definition = "Purple fruit",
                example = "I'm eating a fig."
            )
        ),
        favorite = true
    )

    val orange = WordDTO(
        word = "Orange",
        phoneticsText = "ˈɒrɪnʤ",
        phoneticsAudio = null,
        meanings = listOf(
            MeaningDTO(
                partOfSpeech = "Substantive",
                definition = "Orange fruit",
                example = "I'm eating an orange."
            )
        ),
        favorite = false
    )

    @Test
    fun getHistoryApiStatus() {
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test getFavoriteWordsFromDatabase`(): Unit = runTest {

        // Given
        val objectUnderTest = HistoryViewModel(provider)

        val wordsDTOList = listOf(banana, fig)

        coEvery { provider.getFavoriteWords() } returns wordsDTOList

        // When
        objectUnderTest.getFavoriteWordsFromDatabase()

        // Then
        coVerify(exactly = 1) { provider.getFavoriteWords() }
    }

    @Test
    fun getPreviouslySearchedWords() {
    }

    @Test
    fun toggleFavoriteWord() {
    }

    @Test
    fun getRandomPreviouslySearchedWordEntry() {
    }

    @Test
    fun getPreviouslySearchedWordEntry() {
    }
}
