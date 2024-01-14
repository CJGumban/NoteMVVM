package com.example.notemvvm.data

import android.content.Context
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [
                        Note::class,
                        Label::class
                     ],
    version = 13, exportSchema = false)
abstract class NoteRoomDatabase : RoomDatabase(){

    abstract fun noteDao():NoteDao

    companion object{
        private val MIGRATION_11_12 = object : Migration(11, 12) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `label_table` (`label_name` TEXT," +
                        "PRIMARY KEY(`label_name`))")
            }
        }
        private val MIGRATION_12_13 = object : Migration(12, 13) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE  `new_label_table` (`id` INTEGER, " +
                        "`label` TEXT " +
                        "PRIMARY KEY(`id`))")

                database.execSQL("INSERT INTO new_course_table(label" +
                        "SELECT label_name FROM label_table")

                database.execSQL("DROP TABLE label_table")

                database.execSQL("ALTER TABLE new_label_table RENAME TO label_table")
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
                ).fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_11_12, MIGRATION_12_13).

                build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}