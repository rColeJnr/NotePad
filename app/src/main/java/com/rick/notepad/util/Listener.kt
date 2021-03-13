package com.rick.notepad.util

import android.annotation.SuppressLint
import android.view.View
import androidx.navigation.Navigation.findNavController
import com.rick.notepad.R

@SuppressLint("StaticFieldLeak")


class Listener(view: View) : OnSwipeTouchListener() {
    
    val navController  = findNavController(view)
    
    var destinationId: Int = 1
    
    override fun onSwipeLeft() {
        
        destinationId = navController.currentDestination!!.id
        
        if (destinationId == R.id.mainFragment){
            navController.navigate(R.id.noteListFragment)
        } else if (destinationId == R.id.taskListFragment){
            navController.navigate(R.id.mainFragment)
        }
        
    }
    
    override fun onSwipeRight() {
        
        destinationId = navController.currentDestination!!.id
        
        if (destinationId == R.id.mainFragment){
            navController.navigate(R.id.taskListFragment)
        } else if (destinationId == R.id.noteListFragment){
            navController.navigate(R.id.mainFragment)
        }
    }
}