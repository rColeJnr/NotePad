package com.rick.notepad.db.note

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rick.notepad.model.Note

@Database(
    entities = [Note::class],
    version = 1
)

abstract class NoteDatabase: RoomDatabase() {

    abstract fun noteDao() : NoteDao

}
