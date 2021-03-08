package com.rick.notepad.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rick.notepad.R
import com.rick.notepad.databinding.FragmentTaskListBinding
import com.rick.notepad.databinding.TaskItemBinding
import com.rick.notepad.model.Task
import com.rick.notepad.ui.MainActivity
import com.rick.notepad.util.Listener
import com.rick.notepad.util.SimpleDividerItemDecoration
import com.rick.notepad.viewmodel.NoteViewModel
import com.rick.notepad.viewmodel.TaskViewModel

class TaskListFragment: Fragment() {
    
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskItemBinding: TaskItemBinding
    
    private lateinit var taskViewModel: TaskViewModel
    private val taskAdapter = TaskAdapter()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    
        binding.rvFragmentTaskList.apply {
            setOnTouchListener(Listener(this))
        }
    
        taskViewModel = (activity as MainActivity).taskViewModel
    
        taskViewModel.taskList.observe(
            viewLifecycleOwner,
            {taskAdapter.differ.submitList(it)}
        )
        
        setuprv()
    }
    
    private fun setuprv(){
        binding.rvFragmentTaskList.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SimpleDividerItemDecoration(context))
        }
    }
    
    inner class TaskAdapter: RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){
        inner class TaskViewHolder(binding: TaskItemBinding):
            RecyclerView.ViewHolder(binding.root)
    
        private val diffUtil = object : DiffUtil.ItemCallback<Task>(){
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id
            }
    
            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.task_name == newItem.task_name
            }
        }
        
        val differ = AsyncListDiffer(this, diffUtil)
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
            taskItemBinding = TaskItemBinding.inflate(LayoutInflater.from(parent.context))
            return TaskViewHolder(taskItemBinding)
        }
    
        override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
            val task = differ.currentList.get(position)
            with(holder){
                with(taskItemBinding){
                    this.tvTaskName.text = task.task_name
                    this.tvTaskDescription.text = task.task_description
                    this.cbTaskCompleted.isChecked = task.task_completed
                    this.tvTaskTime.text = task.task_time
                }
            }
        }
    
        override fun getItemCount() = differ.currentList.size
    }
    
    
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}