package com.example.coodeshchallenge_wordsearch.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.coodeshchallenge_wordsearch.data.sources.local.entities.DictionaryEntryEntity
import com.example.coodeshchallenge_wordsearch.databinding.WordItemBinding

class PagedWordListAdapter(private val clickListener: PagedWordClickedListener) :
    PagingDataAdapter<DictionaryEntryEntity, PagedWordListAdapter.PagedWordsListViewHolder>(PagedWordListAdapter) {

    inner class PagedWordsListViewHolder(private val binding: WordItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dictionaryEntry: DictionaryEntryEntity) {
            binding.wordItemTv.text = dictionaryEntry.word
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagedWordsListViewHolder {
        return PagedWordsListViewHolder(
            WordItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PagedWordsListViewHolder, position: Int) {
        val wordItem = getItem(position)
        if (wordItem != null) {
            holder.itemView.setOnClickListener {
                clickListener.onClick(wordItem)
            }
            holder.bind(wordItem)
        }
    }

    companion object PagedWordsListDiffCallback : DiffUtil.ItemCallback<DictionaryEntryEntity>() {
        override fun areItemsTheSame(oldItem: DictionaryEntryEntity, newItem: DictionaryEntryEntity): Boolean {
            // Id is unique.
            return oldItem.word == newItem.word
        }

        override fun areContentsTheSame(oldItem: DictionaryEntryEntity, newItem: DictionaryEntryEntity): Boolean {
            return oldItem == newItem
        }
    }

    class PagedWordClickedListener(val clickListener: (dictionaryEntry: DictionaryEntryEntity) -> Unit) {
        fun onClick(dictionaryEntry: DictionaryEntryEntity) = clickListener(dictionaryEntry)
    }
}

