package com.example.notemvvm

import android.app.Application
import com.example.notemvvm.data.NoteRoomDatabase
import com.example.notemvvm.data.db.repositories.NoteRepositoryImpl
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NoteApplication : Application()  {

    val database: NoteRoomDatabase by lazy { NoteRoomDatabase.getDatabase(this) }
    val repository by lazy { NoteRepositoryImpl(database.dao()) }

}