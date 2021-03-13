package com.rick.notepad.util.popupmenu

import android.content.Context
import android.content.Intent
import com.rick.notepad.model.Note
import com.rick.notepad.model.Task

class ShareOnClick(private val myIt: Any,
                   private val context: Context) {
    
    fun shareIntent() {
        if ( myIt is Task ){
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, myIt.task_name)
                putExtra(Intent.EXTRA_TEXT, myIt.task_description)
            }.also {
                val chooser = Intent.createChooser(
                    it, "share task via"
                )
                chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.applicationContext.startActivity(chooser)
            }
        } else if ( myIt is Note ){
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, myIt.text)
            }.also {
                val chooser = Intent.createChooser(
                    it, "share note via"
                )
                chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.applicationContext.startActivity(chooser)
            }
        }
        
    }
}