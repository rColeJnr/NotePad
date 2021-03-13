package com.rick.notepad.util.popupmenu

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.rick.notepad.R
import com.rick.notepad.model.Note
import com.rick.notepad.model.Task

class OpenOnClick(fragment: Fragment, private val myIt: Any) {
    
    private val navController = findNavController(fragment)
    
    fun onClickOpen(){
        val bundle = Bundle()
        if (myIt is Note){
            bundle.putSerializable("note", myIt)
            navController.navigate(
                R.id.action_global_noteViewFragment, bundle
            )
//            navController.popBackStack()
        }
        else if (myIt is Task) {
            bundle.putSerializable("task", myIt)
            navController.navigate(
                R.id.action_global_taskViewFragment, bundle
            )
        }
    }
   
    
}