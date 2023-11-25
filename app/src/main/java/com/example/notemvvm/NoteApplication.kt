package com.example.notemvvm

import android.app.Application
import com.example.notemvvm.data.NoteRoomDatabase

class NoteApplication : Application()  {

    val database: NoteRoomDatabase by lazy { NoteRoomDatabase.getDatabase(this) }

}