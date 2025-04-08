package com.sultan.note.view.interfaces

import com.sultan.note.model.data.models.Note;

interface OnClickNote {
   fun onLongClick(note: Note)
   fun onClick(note : Note)
}