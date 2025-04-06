package com.sultan.note.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sultan.note.databinding.StoreHolderBinding
import com.sultan.note.utils.StoreDiffCallback

class StoreAdapter : ListAdapter<String, StoreAdapter.ViewHolder>(StoreDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(StoreHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding : StoreHolderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(text : String) {
            binding.text.setText(text)
        }
    }

}