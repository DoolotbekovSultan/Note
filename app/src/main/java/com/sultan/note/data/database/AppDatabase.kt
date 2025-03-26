package com.sultan.note.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sultan.note.data.database.dao.NoteDao
import com.sultan.note.data.models.Note

@Database(entities = [Note::class  ], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao() : NoteDao
}