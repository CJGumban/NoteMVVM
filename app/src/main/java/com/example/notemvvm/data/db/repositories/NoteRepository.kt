package com.example.notemvvm.data.db.repositories

import com.example.notemvvm.data.db.entities.Label
import com.example.notemvvm.data.db.entities.Note
import com.example.notemvvm.data.db.entities.relationship.NoteLabelCrossRef
import kotlinx.coroutines.flow.Flow

interface NoteRepository {


    fun getAllNotes(): Flow<List<Note>>

    suspend fun searchNotesByText(search: String): List<Note>?

    suspend fun getNoteById(noteId: Int): Note


    suspend fun insertNote(note: Note): Long

    suspend fun updateNote(note: Note)

    suspend fun updatePinnedNote(pinned: Boolean, noteId: Int)
    suspend fun deleteNote(note: Note)

    //labelquery
    fun getAllLabel(): Flow<List<Label>>
    suspend fun _getAllLabel(): List<Label>
    suspend fun upsertLabel(label: Label)

    suspend fun updateLabel(label: Label)

    suspend fun deleteLabel(label: Label)

    fun getAllNoteLabelCrossRef(): Flow<List<NoteLabelCrossRef>>
    suspend fun insertNoteLabelCrossRef(crossRef: NoteLabelCrossRef)

    suspend fun updateNoteLabelCrossRef(noteLabelCrossRef: NoteLabelCrossRef)

    suspend fun deleteNoteLabelCrossRef(noteLabelCrossRef: NoteLabelCrossRef)
    suspend fun deleteNoteLabelCrossRefByNoteId(noteId: Int)

    suspend fun deleteNoteLabelCrossRefByLabelId(labelId: Int)
//Get all notes with or without label join NoteLabelCrossRef & Label table

    suspend fun filterNotesByLabel(labelId: Int): List<Note>?

}