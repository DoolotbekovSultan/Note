package com.sultan.note.ui.models

import java.io.Serializable

data class OnboardPage(
    val animation : Int,
    val title : String,
    val text : String
) : Serializable
