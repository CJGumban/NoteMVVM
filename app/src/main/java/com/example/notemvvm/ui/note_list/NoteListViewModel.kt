package com.example.notemvvm.ui.note_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notemvvm.data.db.entities.Label
import com.example.notemvvm.data.db.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(NotesUiState())
    val uiState:StateFlow<NotesUiState> = _uiState.asStateFlow()
companion object{
    const val TAG = "NoteListViewModel"
}

    init {
        viewModelScope.launch {
            repository.getAllNotes().collect { notes ->
                Log.i(TAG, "init getAllNotes $notes")
                _uiState.update {
                    it.copy(notes = notes)
                }
            }
        }
        viewModelScope.launch {
            repository.getAllLabel().collect { labels ->
                Log.i(TAG, "init getAllLabels $labels")
                _uiState.update {
                    it.copy(labelItems = labels)
                }
            }
        }

    }

    private fun filterNotesByLabel(label: Label) {
        viewModelScope.launch {
            repository.filterNotesByLabel(label.labelId).let { notes ->
                _uiState.update {
                    it.copy(
                        searchMode = false,
                        filterByLabelMode = true,
                        notes = notes,
                        message = "Label: ${label.label}"
                    )
                }
            }

        }
    }

    private fun searchNotesByText(text: String) {
        viewModelScope.launch {
            repository.searchNotesByText(text).let { notes ->
                _uiState.update {
                    it.copy(
                        notes = notes
                    )
                }
            }
        }
    }
    private fun getAllNotes() {
        Log.i(TAG, "get all notes triggered")
        _uiState.update {
            it.copy(
                searchMode = false,
                filterByLabelMode = false,
            )
        }
        viewModelScope.launch {
            repository.getAllNotes().collect { notes ->
                _uiState.update {
                    it.copy(
                        notes = notes
                    )
                }
            }
        }
    }

    fun onEvent(event: NoteListEvent) {

        when (event) {
            is NoteListEvent.OnLabelClick -> {
                filterNotesByLabel(event.label)
            }

            is NoteListEvent.OnSearchTextChange -> {
                searchNotesByText(event.text)
            }

            is NoteListEvent.OnShowAllNotes -> {
                _uiState.update {
                    it.copy(
                        message = "All Notes"
                    )
                }
                getAllNotes()
            }

            NoteListEvent.OnSearchViewClosed -> {
                getAllNotes()
                _uiState.update { it.copy(searchMode = false) }
            }

            NoteListEvent.OnSearchViewOpened -> {
                _uiState.update { it.copy(searchMode = true, filterByLabelMode = false) }
            }

            NoteListEvent.OnOffFilterByLabel -> {
                _uiState.update { it.copy(filterByLabelMode = false) }
            }
        }
    }


    fun snackBarShowed() {
        _uiState.update {
            it.copy(message = null)
        }
    }


}