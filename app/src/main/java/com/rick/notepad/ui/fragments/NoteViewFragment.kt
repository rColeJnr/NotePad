package com.rick.notepad.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.rick.notepad.R
import com.rick.notepad.databinding.FragmentNewNoteBinding
import com.rick.notepad.model.Note
import com.rick.notepad.ui.MainActivity
import com.rick.notepad.util.popupmenu.ShareOnClick
import com.rick.notepad.viewmodel.NoteViewModel

class NoteViewFragment: Fragment() {

    private var _binding: FragmentNewNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var note: Note
    private val args: NoteViewFragmentArgs by navArgs()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNewNoteBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    
        noteViewModel = (activity as MainActivity).noteViewModel
        note = args.note
        binding.apply {
            tvNewNoteName.setText(note.text)
            tvNewNoteTime.text = note.time
        }
        
    }
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.top_menu_note, menu)
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.deleteNote -> {
                noteViewModel.deleteNote(note)
                findNavController().navigate(R.id.noteListFragment)
                Snackbar.make(binding.root, "Note Deleted", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        noteViewModel.addNote(note)
                    }
                    show()
                }
                true
            }
            R.id.shareNote -> { ShareOnClick(note, requireContext()).shareIntent(); true }
            
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (note.text != binding.tvNewNoteName.text.toString()) {
            note.text = binding.tvNewNoteName.text.toString()
            noteViewModel.addNote(note)
        }
    }
}