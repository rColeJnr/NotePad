package com.rick.notepad.util

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.View
import androidx.navigation.Navigation.findNavController
import com.rick.notepad.R

@SuppressLint("StaticFieldLeak")


class Listener(view: View) : OnSwipeTouchListener() {
    
    val navController  = findNavController(view)
    
    var destinationId: Int = 1
    
    override fun onSwipeLeft() {
        
        destinationId = navController.currentDestination!!.id
        
        Log.i("navigationId", "your current pos - $destinationId")
        
        if (destinationId == R.id.mainFragment){
            Log.i("navigationId", "your current pos - $destinationId")
            navController.navigate(R.id.noteListFragment)
        } else if (destinationId == R.id.taskListFragment){
            navController.navigate(R.id.mainFragment)
        }
        
    }
    
    override fun onSwipeRight() {
        
        destinationId = navController.currentDestination!!.id
        Log.i("navigationId", "your current pos - $destinationId")
        
        if (destinationId == R.id.mainFragment){
            Log.i("navigationId", "your current pos - $destinationId")
            navController.navigate(R.id.taskListFragment)
        } else if (destinationId == R.id.noteListFragment){
            navController.navigate(R.id.mainFragment)
        }
    }
}