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
import com.example.coodeshchallenge_wordsearch.data.sources.remote.DictionaryApiService
import com.example.coodeshchallenge_wordsearch.data.sources.remote.ServiceHelpers.createOkHttpClient
import com.example.coodeshchallenge_wordsearch.data.sources.remote.ServiceHelpers.createService
import com.example.coodeshchallenge_wordsearch.ui.BaseViewModel
import com.example.coodeshchallenge_wordsearch.ui.DictionaryViewModel
import com.example.coodeshchallenge_wordsearch.ui.fragments.HistoryViewModel
import com.example.coodeshchallenge_wordsearch.utils.mappers.MeaningDTOMapper
import com.example.coodeshchallenge_wordsearch.utils.mappers.MeaningEntityMapper
import com.example.coodeshchallenge_wordsearch.utils.mappers.WordDTOMapper
import com.example.coodeshchallenge_wordsearch.utils.mappers.WordEntityMapper
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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
        }

        val dataModule = module {
            factory<DictionaryProvider> { DictionaryProviderImpl(get(), get(), get(), get()) }

            single<DictionaryEntriesRepository> { DictionaryEntriesRepositoryImpl(get(), get(), get()) }

            single<SearchedRepository> { SearchedRepositoryImpl(get()) }

            single { DictionaryEntryDataSource(get()) }

            single { DictionaryDatabase.getDatabase(this@DictionaryApp).searchedDao }

            single { DictionaryDatabase.getDatabase(this@DictionaryApp).dictionaryEntriesDao }
        }

        val networkModule = module {

            // Creates a logging interceptor using OkHttp3
            single { createOkHttpClient(OK_HTTP) }

            // Instantiates a Moshi factory
            single { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }

            // Creates a service using Retrofit
            single { createService<DictionaryApiService>(get(), get(), BASE_URL) }

        }

        startKoin {
            androidLogger()
            androidContext(this@DictionaryApp)
            modules(
                listOf(
                    mappersModule,
                    viewModelModule,
                    dataModule,
                    networkModule
                )
            )
        }
    }

    companion object {
        private const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/"
        private const val OK_HTTP = "Ok Http"
    }
}
