package com.example.coodeshchallenge_wordsearch.data.sources.remote

import com.example.coodeshchallenge_wordsearch.data.sources.remote.networkmodel.Word
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApiService {
    @GET("{word}")
    suspend fun getDefinition(@Path("word") word: String): List<Word>
}
