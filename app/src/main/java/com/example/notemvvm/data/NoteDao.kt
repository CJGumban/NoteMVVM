package com.example.notemvvm.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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




    

}