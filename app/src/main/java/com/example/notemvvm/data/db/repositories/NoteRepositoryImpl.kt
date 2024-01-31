package com.example.notemvvm.data.db.repositories

import androidx.annotation.WorkerThread
import com.example.notemvvm.data.NoteDao
import com.example.notemvvm.data.NoteRoomDatabase
import com.example.notemvvm.data.db.entities.Label
import com.example.notemvvm.data.db.entities.Note
import com.example.notemvvm.data.db.entities.relationship.NoteLabelCrossRef

import kotlinx.coroutines.flow.Flow


//Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class NoteRepositoryImpl(
    private val noteDao: NoteDao
): NoteRepository {


    // Room executes all the queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    override fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()
    override suspend fun searchNotesByText(search: String): List<Note>? {
        TODO("Not yet implemented")
    }

    override suspend fun getNoteById(noteId: Int): Note? {
        TODO("Not yet implemented")
    }

    override suspend fun upsertNote(note: Note) {
        TODO("Not yet implemented")
    }

    override suspend fun updateNote(note: Note) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNote(markedNotes: List<Note>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNote(note: Note) {
        TODO("Not yet implemented")
    }

    override fun getAllLabel(): Flow<List<Label>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertLabel(label: Label) {
        TODO("Not yet implemented")
    }

    override suspend fun updateLabel(label: Label) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteLabel(label: Label) {
        TODO("Not yet implemented")
    }

    override fun getAllNoteLabelCrossRef(): Flow<List<NoteLabelCrossRef>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertNoteLabelCrossRef(crossRef: NoteLabelCrossRef) {
        TODO("Not yet implemented")
    }

    override suspend fun updateNoteLabelCrossRef(noteLabelCrossRef: NoteLabelCrossRef) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNoteLabelCrossRef(noteLabelCrossRef: NoteLabelCrossRef) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNoteLabelCrossRefByNoteId(noteId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNoteLabelCrossRefByLabelId(labelId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun filterNoteByLabel(labelId: Int): Flow<List<Note>> {
        TODO("Not yet implemented")
    }
    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.



}

