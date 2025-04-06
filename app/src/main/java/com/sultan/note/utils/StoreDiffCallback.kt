package com.sultan.note.utils

import androidx.recyclerview.widget.DiffUtil
import com.sultan.note.data.models.Note

class StoreDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}