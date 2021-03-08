package com.rick.notepad.db.note

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rick.notepad.model.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(note: Note)

    @Query("SELECT * FROM table_note")
    fun getNotes(): LiveData<List<Note>>

    @Delete
    suspend fun deleteNote(note: Note)
}