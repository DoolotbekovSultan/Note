package com.sultan.note.utils

import android.content.Context
import android.content.SharedPreferences

class Preference {
    private lateinit var sharedPreferences : SharedPreferences

    companion object {
        private const val IS_FIRST_VISIT = "IS_FIRST_VISIT"
        private const val IS_AUTH = "IS_AUTH"
        private const val LAYOUT_MANAGER = "LAYOUT_MANAGER"
    }

    fun unit(context : Context) {
        sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
    }

    var isFirstVisit : Boolean
        get() = sharedPreferences.getBoolean(IS_FIRST_VISIT, true)
        set(value) = sharedPreferences.edit().putBoolean(IS_FIRST_VISIT, value).apply()

    var layoutManager : Int
        get() = sharedPreferences.getInt(LAYOUT_MANAGER, 0)
        set(value) = sharedPreferences.edit().putInt(LAYOUT_MANAGER, value).apply()

    var isAuth : Boolean
        get() = sharedPreferences.getBoolean(IS_AUTH, true)
        set(value) = sharedPreferences.edit().putBoolean(IS_AUTH, value).apply()
}