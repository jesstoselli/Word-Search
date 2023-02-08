package com.example.coodeshchallenge_wordsearch.data.sources.remote

import android.util.Log
import com.example.coodeshchallenge_wordsearch.data.sources.remote.networkmodel.Word
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/"
private const val OK_HTTP = "Ok Http"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

fun createOkHttpClient(okhttp: String): OkHttpClient {
    val interceptor = HttpLoggingInterceptor {
        Log.e(okhttp, it)
    }
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    return OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()
}

private val okHttpClient = createOkHttpClient(OK_HTTP)

interface DictionaryApiService {
    @GET("{word}")
    suspend fun getDefinition(@Path("word") word: String): Response<List<Word>>
}


object DictionaryApi {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val dictionaryService: DictionaryApiService by lazy {
        retrofit.create(DictionaryApiService::class.java)
    }
}
