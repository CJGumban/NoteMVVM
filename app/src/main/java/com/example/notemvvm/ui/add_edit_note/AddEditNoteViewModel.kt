package com.example.notemvvm.ui.add_edit_note


import androidx.lifecycle.ViewModel
import com.example.notemvvm.data.db.entities.Note
import com.example.notemvvm.data.db.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(private val repository: NoteRepository): ViewModel() {

    var newNote: Boolean = false
    private val _uiState = MutableStateFlow(AddEditNoteUiState())
    val uiState: StateFlow<AddEditNoteUiState> = _uiState.asStateFlow()


    suspend fun loadNote(noteId: Int){
        if (noteId==0){
            newNote = true
        }
        else {
                repository.getNoteById(noteId).let { note ->
                    newNote = false
                    _uiState.update {
                        it.copy(
                            note = note
                        )
                    }
                }
        }
    }

    suspend fun saveNote(note: Note): Int{

        if (newNote) {
            if (note.title.isNotEmpty() || note.body.isNotEmpty()) {
                val currentNote = note.copy(noteId = repository.insertNote(note).toInt())
                loadNote(currentNote.noteId)
                return currentNote.noteId
            }
        } else {
            repository.updateNote(note)
            return note.noteId
        }
       return 0
    }

    suspend fun deleteNote() {
        repository.deleteNote(uiState.value.note)
        repository.deleteNoteLabelCrossRefByNoteId(uiState.value.note.noteId)
    }



    companion object {
        const val TAG = "AddEditNoteViewModel"
    }

}