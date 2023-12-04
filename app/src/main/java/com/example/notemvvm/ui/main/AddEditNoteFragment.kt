package com.example.notemvvm.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.notemvvm.NoteApplication
import com.example.notemvvm.data.Note
import com.example.notemvvm.databinding.FragmentAddEditNoteBinding
import kotlinx.coroutines.launch

class AddEditNoteFragment : Fragment() {
    private var _binding: FragmentAddEditNoteBinding? = null
    private var note: Note? = null
    private val args: AddEditNoteFragmentArgs by navArgs()
    private val binding get() = _binding!!
    private val viewmodel: AppViewModel by activityViewModels {
        AppViewModel.AppViewModelFactory(
            (activity?.application as NoteApplication).database.noteDao()
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.noteId!=0){
            loadNote(args.noteId)
        }
        binding.addeditnoteTopappbar.setNavigationOnClickListener {
            if (noteValidation()){
                saveNote()
            }
        }

    }

    private fun loadNote(noteId: Int){
        val title = binding.titleEdittext
        val body = binding.contentEdittext
        lifecycleScope.launch{
            viewmodel.getNoteById(noteId).collect {
                title.setText(it.title)
                body.setText(it.body)
                note = it
            }
        }

    }

    private fun saveNote(){

        note = Note(0,binding.titleEdittext.text.toString(),binding.contentEdittext.text.toString(),System.currentTimeMillis().toLong(),"null",false)
        viewmodel.addNote(note!!)
    }
    private fun noteValidation():Boolean{
        return binding.titleEdittext.text!=null||binding.contentEdittext.text!=null

    }



}