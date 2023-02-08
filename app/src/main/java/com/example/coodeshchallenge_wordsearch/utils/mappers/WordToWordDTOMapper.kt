package com.example.coodeshchallenge_wordsearch.utils.mappers

import com.example.coodeshchallenge_wordsearch.data.sources.remote.networkmodel.Meaning
import com.example.coodeshchallenge_wordsearch.data.sources.remote.networkmodel.Word
import com.example.coodeshchallenge_wordsearch.ui.model.MeaningDTO
import com.example.coodeshchallenge_wordsearch.ui.model.WordDTO

class WordToWordDTOMapper(private val meaningToMeaningDTOMapper: MeaningToMeaningDTOMapper) : DataMapper<Word, WordDTO>() {
    override fun toDomain(data: Word): WordDTO = data.let {
        WordDTO(
            word = it.word,
            phoneticsText = it.phonetics.first().text,
            phoneticsAudio = it.phonetics.first().audio.orEmpty(),
            meanings = it.meanings.map { meaning -> meaningToMeaningDTOMapper.toDomain(meaning) },
            favorite = false
        )
    }
}

class MeaningToMeaningDTOMapper : DataMapper<Meaning, MeaningDTO>() {
    override fun toDomain(data: Meaning): MeaningDTO = data.let {
        MeaningDTO(
            partOfSpeech = it.partOfSpeech,
            definition = it.definitions.first().definition,
            example = it.definitions.first().example
        )
    }

}
