package com.example.notemvvm.data

import android.content.Context
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [
                        Note::class
                     ],
    version = 11, exportSchema = false)
public abstract class NoteRoomDatabase : RoomDatabase(){

    abstract fun noteDao():NoteDao

    companion object{
/*        val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `label_table` (`label_name` TEXT," +
                        "PRIMARY KEY(`label_name`))")
            }
        }*/
val MIGRATION_10_11 = object : Migration(10, 11) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE `label_table`")
    }
}
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
                ).fallbackToDestructiveMigration().addMigrations(MIGRATION_10_11).

                build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}