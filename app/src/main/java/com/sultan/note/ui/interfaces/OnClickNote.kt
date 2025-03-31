package com.sultan.note.ui.interfaces

import com.sultan.note.data.models.Note;

interface OnClickNote {
   fun onLongClick(note: Note)
   fun onClick(note : Note)
}