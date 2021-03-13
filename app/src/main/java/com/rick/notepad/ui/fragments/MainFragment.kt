package com.rick.notepad.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
import com.rick.notepad.util.popupmenu.MyPopUpMenu
import com.rick.notepad.util.popupmenu.OpenOnClick
import com.rick.notepad.viewmodel.NoteViewModel
import com.rick.notepad.viewmodel.TaskViewModel

class MainFragment: Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteItemView: NoteItemBinding
    private lateinit var taskItemView: TaskItemBinding
    lateinit var noteViewModel: NoteViewModel
    lateinit var taskViewModel: TaskViewModel
    private lateinit var mainList: MutableList<Any>
    private val myadapter = MainFragmentAdapter()
    
    override fun onStart() {
        mainList = mutableListOf()
        super.onStart()
    }
    
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
                mainList.addAll(it)
                myadapter.differ.submitList(mainList.toSet().toList())
            }
        )
        
        taskViewModel.taskList.observe(
            viewLifecycleOwner,
            {
                mainList.addAll(it)
                myadapter.differ.submitList(mainList.toSet().toList())
            }
        )
        
        
        setuprv()
    }

    private fun setuprv(){
        binding.rvMainFragment.apply {
            adapter = myadapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SimpleDividerItemDecoration(context))
            registerForContextMenu(this)
        }
    }
    
    override fun onDestroy() {
        _binding = null
        findNavController().popBackStack().also {
            activity?.finish()
        }
        super.onDestroy()
    }

    inner class MainFragmentAdapter:
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        
        private val diffUtil = object : DiffUtil.ItemCallback<Any>(){
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                return false
            }
    
            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                return false
            }
        }
        
        val differ = AsyncListDiffer(this, diffUtil)
        
        override fun getItemViewType(position: Int): Int {
//            return if (mainList.get(position) is Task) 0
    
            Log.i("differSize", "${differ.currentList.size}")
            return if (differ.currentList[position] is Task) 0
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
//                    val task = mainList.get(position) as Task
                    val task = differ.currentList[position] as Task
                    TaskViewHolder(taskItemView).bindTask(task)
                    
                    itemView.apply {
                        setOnClickListener {
                            OpenOnClick(this@MainFragment, task).onClickOpen()
                        }
                        setOnLongClickListener {
                            MyPopUpMenu(context, task, it, taskViewModel, this@MainFragment).myPopUpMenu()
                            true
                        }
                    }
                    
                }
                if (this is NoteViewHolder) {
//                    val note = mainList.get(position) as Note
                    val note = differ.currentList[position] as Note
                    NoteViewHolder(noteItemView).bindNote(note)
    
                    itemView.apply {
                        setOnClickListener {
                            OpenOnClick(this@MainFragment, note).onClickOpen()
                        }
                        
                        setOnLongClickListener {
                            MyPopUpMenu(context, note, it, this@MainFragment.noteViewModel, this@MainFragment).myPopUpMenu()
                            true
                        }
                    }
                }
            }
        }
        
        override fun getItemCount() = differ.currentList.size
        
        inner class TaskViewHolder(binding: TaskItemBinding):
            RecyclerView.ViewHolder(binding.root){
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