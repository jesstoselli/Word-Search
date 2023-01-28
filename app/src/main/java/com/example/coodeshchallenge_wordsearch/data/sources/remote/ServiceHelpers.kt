package com.example.coodeshchallenge_wordsearch.data.sources.remote

import android.util.Log
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ServiceHelpers {

    fun createOkHttpClient(okhttp: String): OkHttpClient {
        val interceptor = HttpLoggingInterceptor {
            Log.e(okhttp, it)
        }
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    inline fun <reified T> createService(
        client: OkHttpClient,
        factory: Moshi,
        baseUrl: String
    ): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(factory))
            .build()
            .create(T::class.java)
    }

}
