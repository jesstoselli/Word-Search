package com.example.coodeshchallenge_wordsearch.utils.mappers

import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.MeaningEntity
import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.WordEntity
import com.example.coodeshchallenge_wordsearch.ui.model.MeaningDTO
import com.example.coodeshchallenge_wordsearch.ui.model.WordDTO

class WordDTOMapper(private val meaningDTOMapper: MeaningDTOMapper) : DataMapper<WordEntity, WordDTO>() {

    override fun toDomain(data: WordEntity): WordDTO = data.let {
        WordDTO(
            word = it.word,
            phoneticsText = it.phoneticsText,
            phoneticsAudio = it.phoneticsAudio,
            meanings = it.meanings.map { meaning -> meaningDTOMapper.toDomain(meaning) },
            favorite = it.favorite
        )
    }

    fun toDomainList(wordEntities: List<WordEntity>): List<WordDTO> {
        return wordEntities.map { toDomain(it) }
    }
}

class MeaningDTOMapper : DataMapper<MeaningEntity, MeaningDTO>() {

    override fun toDomain(data: MeaningEntity): MeaningDTO = data.let {
        MeaningDTO(
            partOfSpeech = it.partOfSpeech,
            definition = it.definition,
            example = it.example
        )
    }

}
