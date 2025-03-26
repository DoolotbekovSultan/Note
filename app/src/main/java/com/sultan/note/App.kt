package com.sultan.note

import android.app.Application
import androidx.room.Room
import com.sultan.note.data.database.AppDatabase
import com.sultan.note.utils.Preference

class App : Application() {

    companion object {
        var appDatabase : AppDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = Preference()
        sharedPreferences.unit(this)
        getInstance()
    }

    private fun getInstance() : AppDatabase? {
        if (appDatabase == null) {
            appDatabase = applicationContext?.let { context ->
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "note_database"
                ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
            }
        }
        return appDatabase
    }
}