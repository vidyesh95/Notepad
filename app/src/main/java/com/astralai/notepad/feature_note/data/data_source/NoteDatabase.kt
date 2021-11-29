package com.astralai.notepad.feature_note.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.astralai.notepad.feature_note.domain.model.Note

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}