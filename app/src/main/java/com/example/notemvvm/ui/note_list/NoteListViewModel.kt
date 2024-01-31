package com.example.notemvvm.ui.note_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notemvvm.data.db.entities.Label
import com.example.notemvvm.data.db.entities.Note
import com.example.notemvvm.data.db.entities.relationship.NoteLabelCrossRef
import com.example.notemvvm.data.db.repositories.NoteRepository
import com.example.notemvvm.data.db.repositories.NoteRepositoryImpl
import com.example.notemvvm.util.Routes
import com.example.notemvvm.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.inject.Inject


class NoteListViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel() {
    val notes = repository.getAllNotes()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: NoteListEvent) {
        when(event) {
            is NoteListEvent.OnAddLabelClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_NOTE))
            }
            is NoteListEvent.OnAddNoteClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_NOTE))
            }
            is NoteListEvent.OnLabelClick -> {
                TODO()
            }
            is NoteListEvent.OnNoteClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_NOTE + "?noteId=${event.note.noteId}"))
            }
            is NoteListEvent.OnSearchNoteByText -> {
                TODO()
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
    private var _noteEditMode: Boolean? = false
    var noteToEdit: Note? = Note()
    private val noteEditMode get() = _noteEditMode!!
    val allNotes: Flow<List<Note>> = repository.getAllNotes()

    fun onEditMode():Boolean{
        return noteEditMode
    }
    fun updatingNote(note: Note){
        _noteEditMode = true
        this.noteToEdit = note
    }
    suspend fun searchNotesByText(search:String): List<Note>? = repository.searchNotesByText(search)
    fun addNote(note: Note){
        insertNote(note)
    }

    suspend fun getNoteById(noteId: Int): Note? {
        return repository.getNoteById(noteId)
    }
    fun filterNotesByLabel(labelId: Int): Flow<List<Note>>{
        return repository.filterNotesByLabel(labelId)
    }

    private fun insertNote(note: Note) = viewModelScope.launch {
        repository.upsertNote(note)
        Log.i("homeviewModel","${this}")
    }
    fun deleteNote(note: Note) = viewModelScope.launch{
        _noteEditMode = false
        noteToEdit = Note()
        repository.deleteNote(note)
        repository.deleteNoteLabelCrossRefByNoteId(note.noteId)
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        repository.updateNote(note)
    }

    //label_table functions

    fun getAllLabels(): Flow<List<Label>> = repository.getAllLabel()

    fun insertLabel(label: Label) = viewModelScope.launch {
        repository.insertLabel(label)

    }

    fun updateLabel(label: Label) = viewModelScope.launch{
        repository.updateLabel(label)
    }
    fun deleteLabel(label: Label) = viewModelScope.launch {
        repository.deleteLabel(label)
    }

//functions for NoteLabelCrossRef
    fun getAllNoteLabelCrossRef(): Flow<List<NoteLabelCrossRef>> = repository.getAllNoteLabelCrossRef()
    fun addNoteLabelCrossRef(noteLabelCrossRef: NoteLabelCrossRef) = viewModelScope.launch {
        repository.insertNoteLabelCrossRef(noteLabelCrossRef)
    }


    suspend fun  deleteNoteLabelCrossRefByNoteId(noteId: Int) = viewModelScope.launch{
        repository.deleteNoteLabelCrossRefByNoteId(noteId)
    }

    suspend fun deleteNoteLabelCrossRefByLabelId(labelId: Int) = viewModelScope.launch{
        repository.deleteNoteLabelCrossRefByLabelId(labelId)
    }


    class AppViewModelFactory(
        private val repository: NoteRepositoryImpl
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NoteListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NoteListViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


}