package com.rick.notepad.util.popupmenu

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.rick.notepad.R
import com.rick.notepad.model.Note
import com.rick.notepad.model.Task
import com.rick.notepad.viewmodel.NoteViewModel
import com.rick.notepad.viewmodel.TaskViewModel

class MyPopUpMenu(
    private val context: Context,
    private val myIt: Any,
    private val view: View,
    private val viewModel: ViewModel,
    private val fragment: Fragment
) {

    fun myPopUpMenu(){
        if (myIt is Task){
            viewModel as TaskViewModel
            val popupMenu = PopupMenu(context, view)
            popupMenu.inflate(R.menu.click_menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when(item.itemId){
                    R.id.itemDelete -> {
                        viewModel.deleteTask(myIt)
                        Snackbar.make(view, "task deleted", Snackbar.LENGTH_LONG).show()
                    }
            
                    R.id.itemShare -> { ShareOnClick(myIt, context).shareIntent() }
            
                    R.id.itemOpen -> {
                        OpenOnClick(fragment, myIt).onClickOpen()}
                }
                true
            }
            popupMenu.show()
            
        } else if (myIt is Note){
            viewModel as NoteViewModel
            val popupMenu = PopupMenu(context, view)
            popupMenu.inflate(R.menu.click_menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when(item.itemId){
                    R.id.itemDelete -> {
                        viewModel.deleteNote(myIt)
                        Snackbar.make(view, "note deleted", Snackbar.LENGTH_LONG).show()
                    }
                    R.id.itemShare -> { ShareOnClick(myIt, context).shareIntent() }
            
                    R.id.itemOpen -> {
                        OpenOnClick(fragment, myIt).onClickOpen()}
                }
                true
            }
            popupMenu.show()
        }
        
    }

}