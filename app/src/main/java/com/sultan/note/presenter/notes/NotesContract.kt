package com.sultan.note.presenter.notes

import com.sultan.note.model.data.models.Note

interface NotesContract {

    interface View {
        fun showNotes(notes : List<Note>)
        fun showError()
    }

    interface Presenter {
        fun loadNotes()
        fun deleteNote(note : Note)
    }
}