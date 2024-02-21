package com.example.notemvvm.data.db.repositories

import com.example.notemvvm.data.NoteDao
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
    override fun getAllNotes(): Flow<List<Note>> { return noteDao.getAllNotes() }
    override suspend fun searchNotesByText(search: String): List<Note> {
        return noteDao.searchNotesByText(search)
    }

    override suspend fun getNoteById(noteId: Int): Note {
        return noteDao.getNoteById(noteId)
    }


    override suspend fun insertNote(note: Note): Long {
        return noteDao.insertNote(note)
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    override suspend fun updatePinnedNote(pinned: Boolean, noteId: Int){
        noteDao.updatePinnedNote(pinned,noteId)
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    override fun getAllLabel(): Flow<List<Label>> {
        return noteDao.getAllLabel()
    }

    override suspend fun _getAllLabel(): List<Label> {
        return noteDao._getAllLabel()
    }

    override suspend fun upsertLabel(label: Label) {
        noteDao.upsertLabel(label)
    }

    override suspend fun updateLabel(label: Label) {
        noteDao.updateLabel(label)
    }

    override suspend fun deleteLabel(label: Label) {
        noteDao.deleteLabel(label)
    }

    override fun getAllNoteLabelCrossRef(): Flow<List<NoteLabelCrossRef>> {
    return noteDao.getAllNoteLabelCrossRef()
    }

    override suspend fun insertNoteLabelCrossRef(crossRef: NoteLabelCrossRef) {
        noteDao.insertNoteLabelCrossRef(crossRef)
    }

    override suspend fun updateNoteLabelCrossRef(noteLabelCrossRef: NoteLabelCrossRef) {
        noteDao.updateNoteLabelCrossRef(noteLabelCrossRef)
    }

    override suspend fun deleteNoteLabelCrossRef(noteLabelCrossRef: NoteLabelCrossRef) {
        noteDao.deleteNoteLabelCrossRef(noteLabelCrossRef)
    }

    override suspend fun deleteNoteLabelCrossRefByNoteId(noteId: Int) {
        noteDao.deleteNoteLabelCrossRefByNoteId(noteId)
    }

    override suspend fun deleteNoteLabelCrossRefByLabelId(labelId: Int) {
        noteDao.deleteNoteLabelCrossRefByLabelId(labelId)
    }

    override suspend fun filterNotesByLabel(labelId: Int): List<Note> {
    return noteDao.filterNotesByLabel(labelId)}
    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.



}

