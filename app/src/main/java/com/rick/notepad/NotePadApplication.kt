package com.rick.notepad

import android.app.Application
import com.rick.notepad.db.note.NoteRepository
import com.rick.notepad.db.task.TaskRepository

class NotePadApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        NoteRepository.initialize(this)
        TaskRepository.initialize(this)
    }
    
}