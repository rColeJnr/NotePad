package com.rick.notepad.db.task

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rick.notepad.model.Task


@Dao
interface TaskDao {

    @Query("SELECT * FROM table_task")
    fun getTaskList(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertList(task: Task)

    @Delete
    suspend fun deleteList(task: Task)

}