package com.sultan.note

import android.app.Application
import com.sultan.note.utils.Preference

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = Preference()
        sharedPreferences.unit(this)
    }
}