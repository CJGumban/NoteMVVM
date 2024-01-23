package com.example.notemvvm.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notemvvm.data.Label
import com.example.notemvvm.data.Note
import com.example.notemvvm.data.NoteDao
import com.example.notemvvm.data.relationship.NoteLabelCrossRef
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException


class AppViewModel(private val repository: NoteDao) : ViewModel() {
    private var _noteEditMode: Boolean? = false
    var noteToEdit: Note? = Note()
    private val noteEditMode get() = _noteEditMode!!
    val allNotes: Flow<List<Note>> = repository.allNotes()

    fun onEditMode():Boolean{
        return noteEditMode
    }



    fun updatingNote(note: Note){
        _noteEditMode = true
        this.noteToEdit = note
    }


    fun searchNotesByText(search:String): Flow<List<Note>> = repository.searchNotes(search)
    fun addNote(note: Note){
        insertNote(note)
    }

    fun getNoteById(noteId: Int): Flow<Note> {
        return repository.getNoteById(noteId)
    }
    fun filterNotesByLabel(labelId: Int): Flow<List<Note>>{
        return repository.filterNoteByLabel(labelId)
    }

    private fun insertNote(note: Note) = viewModelScope.launch {
        repository.insertNote(note)
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
        private val noteDao: NoteDao
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AppViewModel(noteDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


}