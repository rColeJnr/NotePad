package com.rick.notepad.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rick.notepad.util.Useful.DATABASE_NAME2

@Entity( tableName = DATABASE_NAME2 )
data class Task (

    @PrimaryKey (autoGenerate = true)
    var id: Int? = null,
    var task_name: String = "",
    var task_description: String = "",
    var task_time: String = "",
    var task_completed: Boolean = false

)