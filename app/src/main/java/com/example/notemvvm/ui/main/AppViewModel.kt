package com.example.notemvvm.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notemvvm.data.Label
import com.example.notemvvm.data.Note
import com.example.notemvvm.data.NoteDao
import com.example.notemvvm.data.NoteWithLabel
import com.example.notemvvm.data.relationship.NoteLabelCrossRef
import kotlinx.coroutines.channels.broadcast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class AppViewModel(private val repository: NoteDao) : ViewModel() {


//    Using LiveData and caching what  allNotes return has several benefits:
        /*-We can put an observer on the data(instead of polling for changes) and only
        update the UI when the data actually changes.
        * -repository is completely separated from the UI through the ViewModel.*/

    val allNotes: Flow<List<Note>> = repository.allNotes()

    fun searchNotesByText(search:String): Flow<List<Note>> = repository.searchNotes(search)
    fun addNote(note: Note){
        insert(note)
    }

    fun getNoteById(noteId: Int): Flow<Note> {
        return _getNoteById(noteId)
    }

    private fun _getNoteById(noteId: Int): Flow<Note> = repository.getNoteById(noteId)

    private fun searchNoteLabels(search: String): Flow<List<NoteWithLabel>> = repository.searchNoteLabels(search)
    /*
    Launching a new coroutine to insert the data in a non0blocking  way
    */
    private fun insert(note: Note) = viewModelScope.launch {
        repository.insert(note)
        Log.i("homeviewModel","${this}")
    }
    fun delete(note: List<Note>) = viewModelScope.launch {
        repository.delete(note)
    }
    fun delete(note: Note)= viewModelScope.launch{
        repository.delete(note)
    }

    fun update(note: Note) = viewModelScope.launch {
        repository.update(note)
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

    fun getNoteLabels(): Flow<List<NoteWithLabel>> = repository.getAllNotes()


//functions for NoteLabelCrossRef
    fun getAllNoteLabelCrossRef(): Flow<List<NoteLabelCrossRef>> = repository.getAllNoteLabelCrossRef()
    suspend fun addNoteLabelCrossRef(noteLabelCrossRef: NoteLabelCrossRef) {
        repository.insertNoteLabelCrossRef(noteLabelCrossRef)
    }

    suspend fun removeNoteLabelCrossRef(noteLabelCrossRef: NoteLabelCrossRef) {
        repository.deleteNoteLabelCrossRef(noteLabelCrossRef)
    }

    suspend fun deleteNoteLabelCrossRefByNoteId(noteId: Int){
        repository.deleteNoteLabelCrossRefByNoteId(noteId)
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