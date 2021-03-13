package com.rick.notepad.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rick.notepad.util.Useful.DATABASE_NAME
import java.io.Serializable

@Entity( tableName = DATABASE_NAME)
data class Note(

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var title: String = "",
    var text: String = "",
    var time: String = "00:00:00"

) : Serializable