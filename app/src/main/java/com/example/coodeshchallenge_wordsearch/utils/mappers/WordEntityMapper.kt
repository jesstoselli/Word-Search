package com.example.coodeshchallenge_wordsearch.utils.mappers

import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.MeaningEntity
import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.WordEntity
import com.example.coodeshchallenge_wordsearch.data.sources.remote.networkmodel.Meaning
import com.example.coodeshchallenge_wordsearch.data.sources.remote.networkmodel.Word

class WordEntityMapper(private val meaningEntityMapper: MeaningEntityMapper) : DataMapper<Word, WordEntity>() {

    override fun toDomain(data: Word): WordEntity = data.let {
        WordEntity(
            word = it.word,
            phoneticsText = it.phonetics.first().text,
            phoneticsAudio = it.phonetics.first().audio.orEmpty(),
            meanings = it.meanings.map { meaning -> meaningEntityMapper.toDomain(meaning) },
            favorite = false
        )
    }
}

class MeaningEntityMapper : DataMapper<Meaning, MeaningEntity>() {
    override fun toDomain(data: Meaning): MeaningEntity = data.let {
        MeaningEntity(
            partOfSpeech = it.partOfSpeech,
            definition = it.definitions.first().definition,
            example = it.definitions.first().example
        )
    }
}
