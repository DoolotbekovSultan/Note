package com.sultan.note.data.models

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import com.sultan.note.utils.Date

@Entity(tableName = "note")
data class  Note (
    val title : String,
    val text : String,
    val date : String,
    val color : Int
) {
    @PrimaryKey(autoGenerate = true )
    var id : Int = 0
}
