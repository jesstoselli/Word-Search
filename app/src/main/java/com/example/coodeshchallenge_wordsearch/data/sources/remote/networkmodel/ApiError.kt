package com.example.coodeshchallenge_wordsearch.data.sources.remote.networkmodel

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiError(
    @Json(name = "title") val title: String,
    @Json(name = "message") val message: String,
    @Json(name = "resolution") val resolution: String,
)
