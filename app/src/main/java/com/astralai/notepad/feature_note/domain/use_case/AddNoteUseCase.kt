package com.astralai.notepad.feature_note.domain.use_case

import com.astralai.notepad.feature_note.domain.model.InvalidNoteException
import com.astralai.notepad.feature_note.domain.model.Note
import com.astralai.notepad.feature_note.domain.repository.NoteRepository

class AddNoteUseCase(private val repository: NoteRepository) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            throw InvalidNoteException("The title of note is empty.")
        }
        if (note.content.isBlank()) {
            throw InvalidNoteException("The content is empty.")
        }
        repository.insertNote(note)
    }
}