package com.rick.notepad.db.note

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.rick.notepad.model.Note
import com.rick.notepad.util.Useful.DATABASE_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteRepository(context: Context) {

    private val database: NoteDatabase = Room.databaseBuilder(
        context.applicationContext,
        NoteDatabase::class.java,
        DATABASE_NAME
    ).build()

    val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun upsertNote(note: Note) = coroutineScope.launch {
        database.noteDao().upsert(note)
    }

    fun getNotes(): LiveData<List<Note>> = database.noteDao().getNotes()

    fun deleteNote(note: Note) = coroutineScope.launch {
        database.noteDao().deleteNote(note)
    }

    companion object{
        private var INSTANCE: NoteRepository? = null

        fun initialize(context: Context){
            if (INSTANCE == null){
                INSTANCE = NoteRepository(context)
            }
        }

        fun get(): NoteRepository{
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepositoy not initialized")
        }
    }

}