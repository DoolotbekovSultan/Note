package com.sultan.note.presenter.detail

import com.sultan.note.model.data.models.Note

interface DetailContact {

    interface View {
        fun showNote(note : Note)
        fun showErrorMessage()
    }

    interface Presenter {
        fun loadNote(noteId : Int)
        fun saveNote(note: Note)
        fun updateNote(note: Note)
    }

}