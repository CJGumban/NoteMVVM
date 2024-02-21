package com.example.notemvvm.ui.add_edit_note

import com.example.notemvvm.data.db.entities.Note

data class AddEditNoteUiState(
    val note: Note = Note()
)