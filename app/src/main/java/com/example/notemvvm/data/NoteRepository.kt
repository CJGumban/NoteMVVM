package com.example.notemvvm.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.notemvvm.data.Note
import com.example.notemvvm.data.NoteDao
import com.example.notemvvm.ui.main.HomeViewModel
import kotlinx.coroutines.flow.Flow
import java.lang.IllegalArgumentException


//Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class NoteRepository(private val noteDao: NoteDao) {


    // Room executes all the queries on a separate thread.
//     Observed Flow will notify the observer when the data has changed.
    fun allNotes(): Flow<List<Note>> = noteDao.allNotes()
//     By default Room runs suspend queries off the main thread, therefore, we don't need to
//    implement anything else to ensure we're not doing long running database work
//    off the main thread.


    fun getNotesByLabel(label: String): Flow<List<Note>> = noteDao.getNotesByLabel(label)
    fun searchNotes(search: String): Flow<List<Note>> = noteDao.searchNotes(search)


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    suspend fun deleteNote(markedNotes: List<Note>) {
        noteDao.delete(markedNotes)
    }



}

