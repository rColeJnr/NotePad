package com.rick.notepad.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.rick.notepad.R
import com.rick.notepad.databinding.FragmentNewTaskBinding
import com.rick.notepad.model.Task
import com.rick.notepad.ui.MainActivity
import com.rick.notepad.util.popupmenu.ShareOnClick
import com.rick.notepad.viewmodel.TaskViewModel

class TaskViewFragment: Fragment() {

    private var _binding: FragmentNewTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskViewModel: TaskViewModel
    val args: TaskViewFragmentArgs by navArgs()
    private lateinit var task: Task
    
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNewTaskBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    
        taskViewModel = (activity as MainActivity).taskViewModel
        task = args.task
        binding.apply {
            etNewTaskName.setText(task.task_name)
            etNewTaskDescription.setText(task.task_description)
            etCircle.setColorFilter(Color.BLACK)
            etCircle2.setColorFilter(Color.RED)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.top_menu_task, menu)
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.deleteTask -> {
                taskViewModel.deleteTask(task)
                findNavController().navigate(
                    R.id.taskListFragment
                )
                Snackbar.make(binding.root, "Task Deleted", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        taskViewModel.addTask(task)
                    }
                    show()
                }
                true
            }
            R.id.shareTask -> { ShareOnClick(task, requireContext()).shareIntent(); true}
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (task.task_name !=  binding.etNewTaskName.text.toString() || task.task_description != binding.etNewTaskDescription.text.toString() ) {
            task.task_name = binding.etNewTaskName.text.toString()
            task.task_description = binding.etNewTaskDescription.text.toString()
            task.task_time = binding.ibNewTaskTime.text.toString()
            taskViewModel.addTask(task)
        }
    }
}