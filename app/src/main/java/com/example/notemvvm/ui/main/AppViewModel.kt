package com.example.notemvvm.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notemvvm.data.Note
import com.example.notemvvm.data.NoteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class AppViewModel(private val repository: NoteDao) : ViewModel() {


//    Using LiveData and caching what  allNotes return has several benefits:
        /*-We can put an observer on the data(instead of polling for changes) and only
        update the UI when the data actually changes.
        * -repository is completely separated from the UI through the ViewModel.*/

    val allNotes: Flow<List<Note>> = repository.allNotes()

    fun searchNotesByText(search:String): Flow<List<Note>> {
        return searchNotes(search)
    }
    public fun addNote(note: Note){
        insert(note)
    }

    fun getNoteById(noteId: Int): Flow<Note> {
        return _getNoteById(noteId)
    }

    private fun _getNoteById(noteId: Int): Flow<Note> = repository.getNoteById(noteId)

    private fun getNotesByLabel(label: String): Flow<List<Note>> = repository.getNotesByLabel(label)
    private fun searchNotes(search: String): Flow<List<Note>> = repository.searchNotes(search)
    /*
    Launching a new coroutine to insert the data in a non0blocking  way
    */
    private fun insert(note: Note) = viewModelScope.launch {
        repository.insert(note)
        Log.i("homeviewModel","${this.toString()}")
    }
    private fun delete(note: List<Note>) = viewModelScope.launch {
        repository.delete(note)
    }
    private fun update(note: Note) = viewModelScope.launch {
        repository.update(note)
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