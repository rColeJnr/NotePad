package com.rick.notepad.viewmodel

import androidx.lifecycle.ViewModel
import com.rick.notepad.db.task.TaskRepository
import com.rick.notepad.model.Task

class TaskViewModel: ViewModel() {

    private val taskRepository = TaskRepository.get()

    val taskList = taskRepository.getTasks()

    fun addTask(task: Task){
        taskRepository.upsertTask(task)
    }

    fun deleteTask(task: Task){
        taskRepository.deleteTask(task)
    }

}