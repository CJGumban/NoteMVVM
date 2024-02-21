package com.example.notemvvm.ui.note_list

import com.example.notemvvm.data.db.entities.Label

sealed class NoteListEvent {

    data class OnSearchTextChange(val text: String): NoteListEvent()
    object OnSearchViewOpened: NoteListEvent()
    object OnSearchViewClosed: NoteListEvent()
    data class OnLabelClick(val label: Label): NoteListEvent()
    object OnShowAllNotes: NoteListEvent()
    object OnOffFilterByLabel: NoteListEvent()
}