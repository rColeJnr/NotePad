package com.rick.notepad.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.rick.notepad.R
import com.rick.notepad.databinding.ActivityMainBinding
import com.rick.notepad.util.OnSwipeTouchListener
import com.rick.notepad.viewmodel.NoteViewModel
import com.rick.notepad.viewmodel.TaskViewModel

class MainActivity : AppCompatActivity() {
    
    private lateinit var navController: NavController
    private var destinationId: Int = 1
    
    lateinit var noteViewModel: NoteViewModel
    lateinit var taskViewModel: TaskViewModel
    
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        
        navController = findNavController(R.id.navHostFragment)
        
        binding.bottomMenu.setupWithNavController(navController)
        
        
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.mainFragment -> binding.bottomMenu.visibility = View.VISIBLE
                else -> binding.bottomMenu.visibility = View.GONE
            }
        }
    }
}

/*
* A mechanical engineer with a passion for programming
* */