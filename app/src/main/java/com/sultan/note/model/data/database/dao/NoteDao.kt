package com.sultan.note.model.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sultan.note.model.data.models.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note : Note)

    @Delete
    fun delete(note : Note)

    @Update
    fun replace(note: Note)

    @Query("SELECT * FROM note")
    fun getAll() : LiveData<List<Note>>

    @Query("SELECT * FROM note WHERE id = :id")
    fun getById(id : Int) : Note?
}