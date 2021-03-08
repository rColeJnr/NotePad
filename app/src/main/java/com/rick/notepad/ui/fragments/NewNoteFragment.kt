package com.rick.notepad.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rick.notepad.databinding.FragmentNewNoteBinding
import com.rick.notepad.model.Note
import com.rick.notepad.ui.MainActivity
import com.rick.notepad.util.SetTime.dateFormat
import com.rick.notepad.viewmodel.NoteViewModel

class NewNoteFragment: Fragment() {

    private var _binding: FragmentNewNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteViewModel: NoteViewModel
    private var note = Note()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNewNoteBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        noteViewModel = (activity as MainActivity).noteViewModel
    
        note.time = dateFormat.toString()
        binding.tvNewNoteTime.text = note.time
    }
    
    private fun addToNote(){
        if (binding.tvNewNoteName.text.isNotEmpty()){
            note.text = binding.tvNewNoteName.text.toString()
            note.title = note.text.split("\n")[0]
            
            noteViewModel.addNote(note)
        }
    }
    
    override fun onPause() {
        addToNote()
        super.onPause()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}