package com.example.notemvvm.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_table ORDER BY timestamp ASC")
    fun allNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE label == :label  ORDER BY timestamp ASC")
    fun getNotesByLabel(label: String): Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE body LIKE '%' || :search || '%' " + "OR title LIKE '%' || :search || '%' ORDER BY timestamp ASC")
    fun searchNotes(search: String): Flow<List<Note>>
    @Query("SELECT * FROM note_table WHERE id == :noteId")
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

/*
    @Transaction
    @Query("SELECT * FROM note_table WHERE label = :label")
    suspend fun getLabelWithNotes(label: String): List<LabelWithNotes>
*/

}