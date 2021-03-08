package com.rick.notepad.db.task

import android.content.Context
import androidx.room.Room
import com.rick.notepad.model.Task
import com.rick.notepad.util.Useful.DATABASE_NAME2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class TaskRepository(context: Context) {

    private val database = Room.databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        DATABASE_NAME2
    ).build()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun getTasks() = database.taskDao().getTaskList()

    fun upsertTask(task: Task) = coroutineScope.launch {
        database.taskDao().upsertList(task)
    }

    fun deleteTask(task : Task) = coroutineScope.launch {
        database.taskDao().deleteList(task)
    }

    companion object{
        var INSTANCE: TaskRepository? = null

        fun initialize(context: Context){
            if(INSTANCE == null){
                INSTANCE = TaskRepository(context)
            }
        }

        fun get(): TaskRepository{
            return INSTANCE ?:
            throw IllegalStateException("repository not initialized")
        }
    }

}