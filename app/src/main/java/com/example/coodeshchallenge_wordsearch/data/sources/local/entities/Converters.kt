package com.example.coodeshchallenge_wordsearch.data.sources.local.entities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromMeaningEntityListToString(meaningEntity: List<MeaningEntity?>?): String? {
        if (meaningEntity == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<MeaningEntity?>?>() {}.type
        return gson.toJson(meaningEntity, type)
    }

    @TypeConverter
    fun fromStringToMeaningEntityList(countryLangString: String?): List<MeaningEntity>? {
        if (countryLangString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<MeaningEntity?>?>() {}.type
        return gson.fromJson<List<MeaningEntity>>(countryLangString, type)
    }

}
