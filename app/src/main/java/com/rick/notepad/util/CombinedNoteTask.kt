package com.rick.notepad.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.rick.notepad.model.Note
import com.rick.notepad.model.Task

class CombinedNoteTask(
    idnotelist: LiveData<List<Note>>,
    idtasklist: LiveData<List<Task>>
): MediatorLiveData<Pair<List<Note>, List<Task>>>() {
    
    private var notelist: List<Note> = emptyList()
    private var tasklist: List<Task> = emptyList()
    
    init {
        
        value = Pair(notelist, tasklist)
        
        addSource(idnotelist){
            if (it != null ) notelist = it
            value = Pair(notelist, tasklist)
        }
        
        addSource(idtasklist){
            if (it != null ) tasklist = it
            value = Pair(notelist, tasklist)
        }
        
    }
    
}