package com.sultan.note.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sultan.note.R
import com.sultan.note.data.models.Note
import com.sultan.note.databinding.GridNoteHolderBinding
import com.sultan.note.databinding.LinearNoteHolderBinding
import com.sultan.note.ui.interfaces.OnClickNote
import com.sultan.note.utils.DiffCallback

class NoteAdapter(var viewType: Int, private val onClickNote : OnClickNote) : ListAdapter<Note, RecyclerView.ViewHolder>(DiffCallback() ) {

    companion object {
        const val VIEW_TYPE_LINEAR = 0
        const val VIEW_TYPE_GRID = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_LINEAR) {
            LinearViewHolder(LinearNoteHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            GridViewHolder(GridNoteHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemViewType(position: Int) = viewType

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val note = getItem(position)
        when(holder) {
            is LinearViewHolder -> holder.bind(note)
            is GridViewHolder -> holder.bind(note)
        }

        holder.itemView.setOnLongClickListener {
            onClickNote.onLongClick(note)
            return@setOnLongClickListener true
        }

        holder.itemView.setOnClickListener {
            onClickNote.onClick(note)
        }
    }

    inner class LinearViewHolder(private val binding : LinearNoteHolderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) = with(binding) {
            card.setBackgroundResource(note.color)
            titleTextView.text = note.title
            textTextView.text = note.text
            dateTextView.text = note.date
        }
    }

    fun updateViewType(newViewType: Int) {
        viewType = newViewType
        notifyDataSetChanged()
    }

    inner class GridViewHolder(private val binding : GridNoteHolderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) = with(binding) {
            card.setBackgroundResource(note.color)
            titleTextView.text = note.title
            textTextView.text = note.text
            dateTextView.text = note.date
        }
    }

}