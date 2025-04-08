package com.sultan.note.model.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sultan.note.model.data.database.dao.NoteDao
import com.sultan.note.model.data.models.Note

@Database(entities = [Note::class  ], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao() : NoteDao
}