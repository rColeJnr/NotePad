package com.rick.notepad.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rick.notepad.db.note.NoteRepository
import com.rick.notepad.model.Note

class NoteViewModel: ViewModel() {

    private val noteRepository = NoteRepository.get()

    val notesList: LiveData<List<Note>> = noteRepository.getNotes()

    fun addNote(note: Note){
        noteRepository.upsertNote(note)
    }

    fun deleteNote(note: Note){
        noteRepository.deleteNote(note)
    }


}