package com.example.coodeshchallenge_wordsearch

import android.app.Application
import com.example.coodeshchallenge_wordsearch.data.repository.DictionaryEntriesRepository
import com.example.coodeshchallenge_wordsearch.data.repository.DictionaryEntriesRepositoryImpl
import com.example.coodeshchallenge_wordsearch.data.repository.SearchedRepository
import com.example.coodeshchallenge_wordsearch.data.repository.SearchedRepositoryImpl
import com.example.coodeshchallenge_wordsearch.data.sources.DictionaryProvider
import com.example.coodeshchallenge_wordsearch.data.sources.DictionaryProviderImpl
import com.example.coodeshchallenge_wordsearch.data.sources.local.DictionaryDatabase
import com.example.coodeshchallenge_wordsearch.data.sources.local.DictionaryEntryDataSource
import com.example.coodeshchallenge_wordsearch.ui.BaseViewModel
import com.example.coodeshchallenge_wordsearch.ui.dictionary.DictionaryViewModel
import com.example.coodeshchallenge_wordsearch.ui.favorites.FavoritesViewModel
import com.example.coodeshchallenge_wordsearch.ui.history.HistoryViewModel
import com.example.coodeshchallenge_wordsearch.ui.wordpage.WordPageViewModel
import com.example.coodeshchallenge_wordsearch.utils.mappers.MeaningDTOMapper
import com.example.coodeshchallenge_wordsearch.utils.mappers.MeaningEntityMapper
import com.example.coodeshchallenge_wordsearch.utils.mappers.WordDTOMapper
import com.example.coodeshchallenge_wordsearch.utils.mappers.WordEntityMapper
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class DictionaryApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val mappersModule = module {
            factory { WordEntityMapper(get()) }
            factory { WordDTOMapper(get()) }
            factory { MeaningEntityMapper() }
            factory { MeaningDTOMapper() }
        }

        val viewModelModule = module {
            single { BaseViewModel() }
            viewModel { DictionaryViewModel(get()) }
            viewModel { HistoryViewModel(get()) }
            viewModel { FavoritesViewModel(get()) }
            viewModel { WordPageViewModel(get()) }
        }

        val dataModule = module {
            factory<DictionaryProvider> { DictionaryProviderImpl(get(), get(), get(), get()) }

            single<DictionaryEntriesRepository> { DictionaryEntriesRepositoryImpl(get(), get()) }

            single<SearchedRepository> { SearchedRepositoryImpl(get()) }

            single { DictionaryEntryDataSource(get()) }

            single { DictionaryDatabase.getDatabase(this@DictionaryApp).searchedDao }

            single { DictionaryDatabase.getDatabase(this@DictionaryApp).dictionaryEntriesDao }
        }

        startKoin {
            androidLogger()
            androidContext(this@DictionaryApp)
            modules(
                listOf(
                    mappersModule,
                    viewModelModule,
                    dataModule
                )
            )
        }
    }
}
