package com.rick.notepad.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rick.notepad.databinding.FragmentMainBinding
import com.rick.notepad.databinding.NoteItemBinding
import com.rick.notepad.databinding.TaskItemBinding
import com.rick.notepad.model.Note
import com.rick.notepad.model.Task
import com.rick.notepad.ui.MainActivity
import com.rick.notepad.util.Listener
import com.rick.notepad.util.SimpleDividerItemDecoration
import com.rick.notepad.viewmodel.NoteViewModel
import com.rick.notepad.viewmodel.TaskViewModel

class MainFragment: Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteItemView: NoteItemBinding
    private lateinit var taskItemView: TaskItemBinding
    lateinit var noteViewModel: NoteViewModel
    lateinit var taskViewModel: TaskViewModel
    private var mainList: MutableList<Any> = mutableListOf()
    private var myadapter = MainFragmentAdapter()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    
        binding.rvMainFragment.apply {
            setOnTouchListener(Listener(this))
        }
        
        noteViewModel = (activity as MainActivity).noteViewModel
        taskViewModel = (activity as MainActivity).taskViewModel
        
        noteViewModel.notesList.observe(
            viewLifecycleOwner,
            {
                if (!mainList.containsAll(it)) {
                    mainList.removeAll(it)
                    mainList.addAll(it)
                    myadapter.notifyDataSetChanged()
                    Log.i("list", "am still here bitch\n${mainList.toString()}")
                }
            }
        )
        
        taskViewModel.taskList.observe(
            viewLifecycleOwner,
            {
                if (!mainList.containsAll(it)) {
                    mainList.removeAll(it)
                    mainList.addAll(it)
                    myadapter.notifyDataSetChanged()
                    Log.i("list", "am still here bitch\n${mainList.toString()}")
                }
            }
        )
        
        setuprv()
    
        
//        myadapter.differ.submitList(mainList)
        
    }

    private fun setuprv(){
        binding.rvMainFragment.apply {
            adapter = myadapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SimpleDividerItemDecoration(context))
        }
    }
    
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    inner class MainFragmentAdapter:
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        
//        var bool = false
    
        private val diffUtil = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                return false
            }
            
            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                return false
            }
        }
        
        val differ = AsyncListDiffer(this, diffUtil)
        
        override fun getItemViewType(position: Int): Int {
            return if (mainList.get(position) is Task) 0
            else 1
        }
    
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            
            return if (viewType == 0){
                taskItemView = TaskItemBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
                TaskViewHolder(taskItemView)
            } else {
                noteItemView = NoteItemBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false)
                NoteViewHolder(noteItemView)
            }
        }
    
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            with (holder){
                if (this is TaskViewHolder) {
                    val task = mainList.get(position) as Task
                    Log.i("list", "your task - $task")
                    TaskViewHolder(taskItemView).bindTask(task)
                }
                if (this is NoteViewHolder) {
//                    val note = differ.currentList.get(position) as Note
                    val note = mainList.get(position) as Note
                    Log.i("list", "your note - $note")
                    NoteViewHolder(noteItemView).bindNote(note)
                }
            }
        }
    
        override fun getItemCount() = mainList.size
    
        inner class TaskViewHolder(taskbinding: TaskItemBinding):
            RecyclerView.ViewHolder(taskbinding.root){
                fun bindTask(task: Task){
                    taskItemView.apply {
                        tvTaskName.text = task.task_name
                        tvTaskDescription.text = task.task_description
                        tvTaskTime.text = task.task_time
                        cbTaskCompleted.isChecked = task.task_completed
                    }
                    
                }
            }
        
        inner class NoteViewHolder(binding: NoteItemBinding):
            RecyclerView.ViewHolder(binding.root){
                fun bindNote(note: Note){
                    noteItemView.apply {
                        tvNoteName.text = note.title
                        tvNoteTime.text = note.time
                    }
                }
            }
        }
    
}