package com.example.coodeshchallenge_wordsearch.ui.wordpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coodeshchallenge_wordsearch.databinding.MeaningItemBinding
import com.example.coodeshchallenge_wordsearch.ui.model.MeaningDTO

class MeaningsListAdapter(private val meaningsList: List<MeaningDTO>) :
    RecyclerView.Adapter<MeaningsListAdapter.MeaningsListViewHolder>() {

    override fun getItemCount() = meaningsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeaningsListViewHolder {
        return MeaningsListViewHolder(
            MeaningItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MeaningsListViewHolder, position: Int) {
        holder.bind(meaningsList[position])
    }

    class MeaningsListViewHolder(val binding: MeaningItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(meaning: MeaningDTO) {
            binding.textViewDefinition.text = meaning.partOfSpeech + " - " + meaning.definition
            binding.textViewExample.text = meaning.example

            if (meaning.example.isEmpty()) {
                binding.textViewExampleTitle.visibility = View.GONE
                binding.textViewExample.visibility = View.GONE
            }
        }
    }
}
