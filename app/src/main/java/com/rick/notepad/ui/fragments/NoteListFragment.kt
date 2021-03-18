package com.rick.notepad.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rick.notepad.R
import com.rick.notepad.databinding.FragmentNoteListBinding
import com.rick.notepad.databinding.NoteItemBinding
import com.rick.notepad.model.Note
import com.rick.notepad.ui.MainActivity
import com.rick.notepad.util.Listener
import com.rick.notepad.util.SimpleDividerItemDecoration
import com.rick.notepad.util.popupmenu.MyPopUpMenu
import com.rick.notepad.util.popupmenu.OpenOnClick
import com.rick.notepad.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.note_item.view.*

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
    
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvNoteList.setOnTouchListener(Listener(rvNoteList))
            
            noteListBackButton.setOnClickListener { findNavController().navigate(R.id.action_global_mainFragment) }
            
            fabFragmentTaskList.setOnClickListener { findNavController().navigate(R.id.action_noteListFragment_to_newNoteFragment) }
        }
        
        noteViewModel = (activity as MainActivity).noteViewModel
        
        noteViewModel.notesList.observe(
            viewLifecycleOwner,
            {noteAdapter.differ.submitList(it)}
        )
    
        setupRv()
    }
    
    private fun setupRv(){
        binding.rvNoteList.apply {
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SimpleDividerItemDecoration(context))
            registerForContextMenu(this)
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
            noteItemBinding = NoteItemBinding.inflate(LayoutInflater.from(parent.context))
            return NotesViewHolder(noteItemBinding)
        }
        
        override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
            
            val note = differ.currentList[position]
            with(holder){
                with(note){
                    noteItemBinding.tvNoteName.text = this.title
                    noteItemBinding.tvNoteTime.text = this.time
                    when(note.noteColour){
                        R.string.bluecolour -> noteItemBinding.ibNoteColour.setImageResource(R.drawable.bluecircle)
                        R.string.purplecolour -> noteItemBinding.ibNoteColour.setImageResource(R.drawable.purplecircle)
                        R.string.greencolour -> noteItemBinding.ibNoteColour.setImageResource(R.drawable.greencircle)
                        R.string.yellowcolour -> noteItemBinding.ibNoteColour.setImageResource(R.drawable.yellowcircle)
                        R.string.redcolour -> noteItemBinding.ibNoteColour.setImageResource(R.drawable.redcircle)
                    }
                }
                
                this.itemView.apply {
                    tvNoteName.setOnClickListener {
                        OpenOnClick(this@NoteListFragment, note).onClickOpen()
                    }
                    tvNoteName.setOnLongClickListener {
                        MyPopUpMenu(context, note, it, noteViewModel, this@NoteListFragment).myPopUpMenu()
                        true
                    }

                    ibNoteColour.setOnClickListener {
                        PopupMenu(context, it).apply {
                            inflate(R.menu.colours_menu)
                            setOnMenuItemClickListener {
                                when(it.itemId){
                                    R.id.blueColour -> {
                                        ibNoteColour.setImageResource(R.drawable.bluecircle)
                                        note.noteColour = R.string.bluecolour
                                        noteViewModel.addNote(note)
                                    }
                                    R.id.purpleColour -> {
                                        ibNoteColour.setImageResource(R.drawable.purplecircle)
                                        note.noteColour = R.string.purplecolour
                                        noteViewModel.addNote(note)
                                    }
                                    R.id.yellowColour -> {
                                        ibNoteColour.setImageResource(R.drawable.yellowcircle)
                                        note.noteColour = R.string.yellowcolour
                                        noteViewModel.addNote(note)
                                    }
                                    R.id.redColour -> {
                                        ibNoteColour.setImageResource(R.drawable.redcircle)
                                        note.noteColour = R.string.redcolour
                                        noteViewModel.addNote(note)
                                    }
                                    R.id.greenColour -> {
                                        ibNoteColour.setImageResource(R.drawable.greencircle)
                                        note.noteColour = R.string.greencolour
                                        noteViewModel.addNote(note)
                                    }
                                }
                                true
                            }
                            show()
                        }
                    }
                }
            }
        }
        
        override fun getItemCount() = differ.currentList.size
        
    }
    
    override fun onDestroy() {
        _binding = null
        findNavController().popBackStack(R.id.mainFragment, false)
        super.onDestroy()
    }
}

//private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
//    if (keyCode == KeyEvent.KEYCODE_ENTER) {
//        // Hide the keyboard
//        val inputMethodManager =
//            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
//        return true
//    }
//    return false
//}
