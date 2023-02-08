package com.example.coodeshchallenge_wordsearch.data.sources.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.coodeshchallenge_wordsearch.data.sources.local.dao.DictionaryEntriesDao
import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.DictionaryEntryEntity

class DictionaryEntryDataSource(private val dictionaryEntriesDao: DictionaryEntriesDao) : PagingSource<Int,
        DictionaryEntryEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DictionaryEntryEntity> {
        val currentPage = params.key ?: 0
        val items = dictionaryEntriesDao.getWordItems(params.loadSize, currentPage * params.loadSize)
        val nextPage = if (items.isEmpty()) null else currentPage + 1
        return LoadResult.Page(
            data = items,
            prevKey = null,
            nextKey = nextPage
        )
    }

    override fun getRefreshKey(state: PagingState<Int, DictionaryEntryEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPageIndex = state.pages.indexOf(state.closestPageToPosition(anchorPosition))
            state.pages.getOrNull(anchorPageIndex + 1)?.prevKey ?: state.pages.getOrNull(anchorPageIndex - 1)?.nextKey
        }
    }
}
