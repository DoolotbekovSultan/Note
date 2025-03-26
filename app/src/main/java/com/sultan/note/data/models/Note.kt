package com.sultan.note.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class  Note (
    val title : String,
    val text : String,
    val dateString : String

) {
    @PrimaryKey(autoGenerate = true )
    var id : Int = 0
}
