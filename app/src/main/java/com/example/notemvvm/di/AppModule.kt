package com.example.notemvvm.di

import android.app.Application
import androidx.room.Room
import com.example.notemvvm.data.NoteRoomDatabase
import com.example.notemvvm.data.db.repositories.NoteRepository
import com.example.notemvvm.data.db.repositories.NoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteRoomDatabase{
        return Room.databaseBuilder(
            app,
            NoteRoomDatabase::class.java,
            "note_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteRoomDatabase): NoteRepository{
        return NoteRepositoryImpl(db.dao)
    }
}