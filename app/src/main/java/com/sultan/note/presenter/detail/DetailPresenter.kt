package com.sultan.note.presenter.detail

import com.sultan.note.App
import com.sultan.note.R
import com.sultan.note.model.data.models.Note

class DetailPresenter(private val view : DetailContact.View) : DetailContact.Presenter {
    override fun loadNote(noteId: Int) {
        val note = App.appDatabase?.noteDao()?.getById(noteId) ?: Note("", "", "", R.color.yellow)
        view.showNote(note)
    }

    override fun saveNote(note: Note) {
        App.appDatabase?.noteDao()?.insert(note)
    }

    override fun updateNote(note: Note) {
        App.appDatabase?.noteDao()?.replace(note)
    }
}