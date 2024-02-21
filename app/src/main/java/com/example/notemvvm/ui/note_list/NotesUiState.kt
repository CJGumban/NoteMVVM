package com.example.notemvvm.ui.note_list

import com.example.notemvvm.data.db.entities.Label
import com.example.notemvvm.data.db.entities.Note as Note

data class NotesUiState(

    val notes: List<Note>? = listOf<Note>(),
    val labelItems: List<Label> = listOf<Label>(),
    val searchMode: Boolean = false,
    val filterByLabelMode: Boolean = false,
    val message: String? = null


)
