package com.rick.notepad.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rick.notepad.databinding.FragmentNewTaskBinding
import com.rick.notepad.model.Task
import com.rick.notepad.ui.MainActivity
import com.rick.notepad.viewmodel.TaskViewModel

class NewTaskFragment: Fragment() {

    private var _binding: FragmentNewTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskViewModel: TaskViewModel
    private var task = Task()
    
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNewTaskBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel = (activity as MainActivity).taskViewModel
        
        
    }
    
    private fun addToTask(){
        if (binding.etNewTaskName.text.isNotEmpty() || binding.etNewTaskDescription.text.isNotEmpty()){
            binding.apply {
                task.task_name = this.etNewTaskName.text.toString()
                task.task_description = this.etNewTaskDescription.text.toString()
            }
            
            taskViewModel.addTask(task)
        }
    }
    
    override fun onPause() {
        addToTask()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}