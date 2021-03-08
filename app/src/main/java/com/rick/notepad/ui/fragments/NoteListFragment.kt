package com.rick.notepad.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rick.notepad.databinding.FragmentNoteListBinding
import com.rick.notepad.databinding.NoteItemBinding
import com.rick.notepad.model.Note
import com.rick.notepad.ui.MainActivity
import com.rick.notepad.util.Listener
import com.rick.notepad.util.SimpleDividerItemDecoration
import com.rick.notepad.viewmodel.NoteViewModel

class NoteListFragment: Fragment() {
    
    private var _binding: FragmentNoteListBinding? = null
    // this property is only valid between onCreate and onDestroy
    private val binding get() = _binding!!
    private lateinit var noteItemBinding: NoteItemBinding
    
    private lateinit var noteViewModel: NoteViewModel
    private val noteAdapter = NotesAdapter()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    
        binding.rvNoteList.apply {
            setOnTouchListener(Listener(this))
        }
        
        noteViewModel = (activity as MainActivity).noteViewModel
        
        noteViewModel.notesList.observe(viewLifecycleOwner, {
            noteAdapter.differ.submitList(it)
        })
    
        setupRv()
    }
    
    private fun setupRv(){
        binding.rvNoteList.apply {
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SimpleDividerItemDecoration(context))
        }
    }
    
    inner class NotesAdapter: RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
        
        inner class NotesViewHolder(binding: NoteItemBinding) :
            RecyclerView.ViewHolder(binding.root)
        
        private val listDifferCallback = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note) =
                oldItem.id == newItem.id
            
            override fun areContentsTheSame(oldItem: Note, newItem: Note) =
                oldItem == newItem
        }
        
        val differ = AsyncListDiffer(this, listDifferCallback)
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
            noteItemBinding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return NotesViewHolder(noteItemBinding)
        }
        
        private var onItemClickListener: ((Note) -> Unit)? = null
        private var onMyItemLongClickListener: ((Note) -> Unit)? = null
        
        override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
            
            val note = differ.currentList[position]
            with(holder){
                with(note){
                    noteItemBinding.tvNoteName.text = this.title
                    noteItemBinding.tvNoteTime.text = this.time
                }
                
                this.itemView.apply {
                    setOnClickListener {
                        onItemClickListener?.let {it(note)}
                    }
                    setOnLongClickListener {
                        onMyItemLongClickListener?.let { it(note) }
                        true
                    }
                }
            }
        }
        
        override fun getItemCount() = differ.currentList.size
        
        fun setOnItemClickListener(listener: (Note) -> Unit) {
            onItemClickListener = listener
        }
        
        fun setOnItemLongClickListener(listener: (Note) -> Unit){
            onMyItemLongClickListener = listener
        }
    }
}