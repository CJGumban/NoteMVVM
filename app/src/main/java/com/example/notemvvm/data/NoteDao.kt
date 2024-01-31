package com.example.notemvvm.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.notemvvm.data.db.entities.Label
import com.example.notemvvm.data.db.entities.Note
import com.example.notemvvm.data.db.entities.relationship.NoteLabelCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_table ORDER BY timestamp ASC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE body LIKE '%' || :search || '%' " + "OR title LIKE '%' || :search || '%' ORDER BY timestamp ASC")
    suspend fun searchNotesByText(search: String): List<Note>?

    @Query("SELECT * FROM note_table WHERE noteid == :noteId")
    suspend fun getNoteById(noteId: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun upsertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(markedNotes: List<Note>)

    @Delete
    suspend fun deleteNote(note: Note)


    //labelquery

    @Query("SELECT * FROM label_table")
    fun getAllLabel(): Flow<List<Label>>
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertLabel(label: Label)

    @Update
    suspend fun updateLabel(label: Label)

    @Delete
    suspend fun deleteLabel(label: Label)

    @Query("SELECT * FROM NoteLabelCrossRef")
    fun getAllNoteLabelCrossRef(): Flow<List<NoteLabelCrossRef>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteLabelCrossRef(crossRef: NoteLabelCrossRef)

    @Update
    suspend fun updateNoteLabelCrossRef(noteLabelCrossRef: NoteLabelCrossRef)

    @Delete
    suspend fun deleteNoteLabelCrossRef(noteLabelCrossRef: NoteLabelCrossRef)
    @Query("DELETE FROM NoteLabelCrossRef WHERE noteId = :noteId ")
    suspend fun deleteNoteLabelCrossRefByNoteId(noteId: Int)

    @Query("DELETE FROM NoteLabelCrossRef WHERE labelId = :labelId ")
    suspend fun deleteNoteLabelCrossRefByLabelId(labelId: Int)
//Get all notes with or without label join NoteLabelCrossRef & Label table

    @Query("SELECT note_table.noteId AS noteId, note_table.title AS title, note_table.body AS body, note_table.timestamp AS timestamp, note_table.pinned AS pinned FROM note_table INNER JOIN notelabelcrossref ON notelabelcrossref.noteId = note_table.noteId INNER JOIN label_table ON label_table.labelId = notelabelcrossref.labelId WHERE label_table.labelId = :labelId ORDER BY timestamp ASC")
    suspend fun filterNoteByLabel(labelId: Int): List<Note>?


    //@Query("SELECT note_table.noteId AS noteId, note_table.title AS title, note_table.body AS body, note_table.timestamp AS timestamp, note_table.pinned AS pinned FROM note_table LEFT JOIN notelabelcrossref ON note_table.noteId = notelabelcrossref.noteId LEFT JOIN label_table ON label_table.labelId = notelabelcrossref.labelId")

    /*
        @Transaction
        @Query("SELECT * FROM note_table WHERE label = :label")
        suspend fun getLabelWithNotes(label: String): List<LabelWithNotes>
    */

}