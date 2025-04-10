package com.sultan.note.presenter.notes

import com.sultan.note.App
import com.sultan.note.model.data.models.Note

class NotesPresenter(private val view : NotesContract.View) : NotesContract.Presenter {
    override fun loadNotes() {
        App.appDatabase?.noteDao()?.getAll()?.observeForever { notes ->
            view.showNotes(notes)
        }
    }

    override fun deleteNote(note : Note) {
        App.appDatabase?.noteDao()?.delete(note)
    }
}