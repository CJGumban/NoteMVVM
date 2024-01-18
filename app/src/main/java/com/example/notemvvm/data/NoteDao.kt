package com.example.notemvvm.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.notemvvm.data.relationship.NoteLabelCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_table ORDER BY timestamp ASC")
    fun allNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE body LIKE '%' || :search || '%' " + "OR title LIKE '%' || :search || '%' ORDER BY timestamp ASC")
    fun searchNotes(search: String): Flow<List<Note>>
    @Query("SELECT * FROM note_table WHERE noteid == :noteId")
    fun getNoteById(noteId: Int): Flow<Note>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(markedNotes: List<Note>)

    @Delete
    suspend fun delete(note: Note)


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
    suspend fun updateNoteLabelCrossRef(crossRef: NoteLabelCrossRef)

    @Delete
    suspend fun deleteNoteLabelCrossRef(crossRef: NoteLabelCrossRef)
    @Query("DELETE FROM NoteLabelCrossRef WHERE noteId = :noteId ")
    suspend fun deleteNoteLabelCrossRefByNoteId(noteId: Int)

    @Query("DELETE FROM NoteLabelCrossRef WHERE labelId = :labelId ")
    suspend fun deleteNoteLabelCrossRefByLabelId(labelId: Int)
//Get all notes with or without label join NoteLabelCrossRef & Label table
    @Query("SELECT note_table.noteId AS noteId, note_table.title AS title, note_table.body AS body, note_table.timestamp AS timestamp, note_table.pinned AS pinned, label_table.labelId AS labelId, label_table.label AS label FROM note_table LEFT JOIN notelabelcrossref ON notelabelcrossref.noteId = note_table.noteId LEFT JOIN label_table ON label_table.labelId = notelabelcrossref.labelId")
    fun getAllNotes(): Flow<List<NoteWithLabel>>

    @Query("SELECT note_table.noteId AS noteId, note_table.title AS title, note_table.body AS body, note_table.timestamp AS timestamp, note_table.pinned AS pinned, label_table.labelId AS labelId, label_table.label AS label FROM note_table LEFT JOIN notelabelcrossref LEFT JOIN label_table ON label_table.labelId = notelabelcrossref.labelId WHERE body LIKE '%' || :search || '%' " + "OR title LIKE '%' || :search || '%' ORDER BY timestamp ASC")
    fun searchNoteLabels(search: String): Flow<List<NoteWithLabel>>

/*
    @Transaction
    @Query("SELECT * FROM note_table WHERE label = :label")
    suspend fun getLabelWithNotes(label: String): List<LabelWithNotes>
*/

}