package com.astralai.notepad.feature_note.data.data_source

import androidx.room.*
import com.astralai.notepad.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query(value = "SELECT * FROM note")
    fun getNotes(): Flow<List<Note>>

    @Query(value = "SELECT * FROM note WHERE id = :id")
    suspend fun getNoteBYId(id: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}