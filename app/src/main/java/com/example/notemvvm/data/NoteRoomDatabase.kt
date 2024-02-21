package com.example.notemvvm.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.notemvvm.data.db.entities.Label
import com.example.notemvvm.data.db.entities.Note
import com.example.notemvvm.data.db.entities.relationship.NoteLabelCrossRef


@Database(entities = [
                        Note::class,
                        Label::class,
                        NoteLabelCrossRef::class
                     ],
    version = 2, exportSchema = false)
abstract class NoteRoomDatabase : RoomDatabase(){

    abstract val noteDao:NoteDao

   /* companion object{
        //Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null
        fun getDatabase(context: Context): NoteRoomDatabase {

            //if the INSTANCE is not null, then returns it
            //if it isn't then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteRoomDatabase::class.java,
                    "note_database"
                ).fallbackToDestructiveMigration().
                build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }*/
}