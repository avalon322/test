package com.example.noteapp.model

object NoteRepository {
    private val notes = mutableListOf<Note>()

    fun save(note: Note) {
        notes.add(note)
    }

    fun all(): List<Note> = notes
}
