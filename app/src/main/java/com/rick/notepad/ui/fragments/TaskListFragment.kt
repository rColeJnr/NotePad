package com.rick.notepad.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
import com.rick.notepad.util.popupmenu.MyPopUpMenu
import com.rick.notepad.util.popupmenu.OpenOnClick
import com.rick.notepad.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.task_item.view.*

class TaskListFragment: Fragment() {
    
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskItemBinding: TaskItemBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var task: Task
    private val taskAdapter = TaskAdapter()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    
        binding.apply {
            rvFragmentTaskList.setOnTouchListener(Listener(rvFragmentTaskList))
            
            taskListBackButton.setOnClickListener { findNavController().navigate(R.id.action_global_mainFragment) }
            
            fabFragmentTaskList.setOnClickListener { findNavController().navigate(R.id.action_taskListFragment_to_newTaskFragment) }
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
            task = differ.currentList.get(position)
            with(holder){
                with(taskItemBinding){
                    this.tvTaskName.text = task.task_name
                    this.tvTaskDescription.text = task.task_description.split("\n")[0]
                    this.cbTaskCompleted.isChecked = task.task_completed
                    this.tvTaskTime.text = task.task_time
                }
                
                itemView.apply {
                    taskItemBinding.tvTaskName.setOnClickListener {
                        OpenOnClick(this@TaskListFragment, task).onClickOpen()
                    }
    
                    taskItemBinding.tvTaskName.setOnLongClickListener {
                        MyPopUpMenu(context, task, it, taskViewModel, this@TaskListFragment)
                        true
                    }
                    
                    cbTaskCompleted.setOnClickListener {
                        task = differ.currentList.get(position) as Task
                        task.task_completed = cbTaskCompleted.isChecked
                        taskViewModel.addTask(task)
                        Log.e("task", "${task.task_completed}")
                    }
                }
                
            }
        }
    
        override fun getItemCount() = differ.currentList.size
    }
    
    override fun onDestroy() {
        findNavController().popBackStack(R.id.mainFragment, false)
        _binding = null
        super.onDestroy()
    }
}