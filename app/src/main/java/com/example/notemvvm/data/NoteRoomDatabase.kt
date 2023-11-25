package com.example.notemvvm.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Note::class],
    version = 1, exportSchema = false)
public abstract class NoteRoomDatabase : RoomDatabase(){

    abstract fun noteDao():NoteDao

    companion object{
        //Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null

        fun getDatabase(context: Context): NoteRoomDatabase {
            //if the INSTANCE is not null, then returns it
            //if it ism then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteRoomDatabase::class.java,
                    "note_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}