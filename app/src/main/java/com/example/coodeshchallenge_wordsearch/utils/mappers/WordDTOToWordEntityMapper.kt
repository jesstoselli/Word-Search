package com.example.coodeshchallenge_wordsearch.utils.mappers

import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.MeaningEntity
import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.WordEntity
import com.example.coodeshchallenge_wordsearch.ui.model.MeaningDTO
import com.example.coodeshchallenge_wordsearch.ui.model.WordDTO

class WordDTOToWordEntityMapper(private val meaningDTOToMeaningEntityMapper: MeaningDTOToMeaningEntityMapper) :
    DataMapper<WordDTO, WordEntity>() {
    override fun toDomain(data: WordDTO): WordEntity = data.let {
        WordEntity(
            word = it.word,
            phoneticsText = it.phoneticsText,
            phoneticsAudio = it.phoneticsAudio.orEmpty(),
            meanings = it.meanings.map { meaning -> meaningDTOToMeaningEntityMapper.toDomain(meaning) },
            favorite = false
        )
    }
}


class MeaningDTOToMeaningEntityMapper : DataMapper<MeaningDTO, MeaningEntity>() {

    override fun toDomain(data: MeaningDTO): MeaningEntity = data.let {
        MeaningEntity(
            partOfSpeech = it.partOfSpeech,
            definition = it.definition,
            example = it.example
        )
    }

}
