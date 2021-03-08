package com.rick.notepad.db.task

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rick.notepad.model.Task

@Database(
    entities = [Task::class],
    version = 1
)
abstract class TaskDatabase: RoomDatabase() {

     abstract fun taskDao(): TaskDao

}