package com.example.coodeshchallenge_wordsearch.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.coodeshchallenge_wordsearch.databinding.WordItemBinding

class WordsListAdapter(private val clickListener: WordClickedListener) :
    ListAdapter<String, WordsListAdapter.WordsListViewHolder>(WordsListDiffCallback) {

    inner class WordsListViewHolder(private val binding: WordItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(word: String) {
            binding.wordItemTv.text = word
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsListViewHolder {
        return WordsListViewHolder(
            WordItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WordsListViewHolder, position: Int) {
        val wordItem = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener.onClick(wordItem)
        }
        holder.bind(wordItem)
    }

    companion object WordsListDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    class WordClickedListener(val clickListener: (word: String) -> Unit) {
        fun onClick(word: String) = clickListener(word)
    }
}

