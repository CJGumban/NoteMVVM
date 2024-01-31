package com.example.notemvvm.ui.note_list

import com.example.notemvvm.data.db.entities.Note

sealed class NoteListEvent {


    data class OnNoteClick(val note: Note): NoteListEvent()
    object OnAddNoteClick: NoteListEvent()

    data class OnSearchNoteByText(val text: String): NoteListEvent()
    data class OnLabelClick(val labelId: Int): NoteListEvent()
    object OnAddLabelClick: NoteListEvent()
}