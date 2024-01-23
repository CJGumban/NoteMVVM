package com.example.notemvvm.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notemvvm.NoteApplication
import com.example.notemvvm.R
import com.example.notemvvm.data.Note
import com.example.notemvvm.databinding.FragmentAddEditNoteBinding
import kotlinx.coroutines.launch

class AddEditNoteFragment : Fragment() {
    private var _binding: FragmentAddEditNoteBinding? = null
    var note: Note? = Note()
    private val args: AddEditNoteFragmentArgs by navArgs()
    private val binding get() = _binding!!
    private val viewModel: AppViewModel by activityViewModels {
        AppViewModel.AppViewModelFactory(
            (activity?.application as NoteApplication).database.noteDao()
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //to check if an id was passed from the note adapter which means it is not a new
        if (args.noteId != 0) {
            loadNote(args.noteId)
        }
    //   TODO() fix note implementation

        binding.addeditnoteTopappbar.setNavigationOnClickListener {
            noteValidation()
            view.findNavController().navigateUp()
        }

        binding.addeditnoteTopappbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.label -> {
                   editNoteLabel()
                    true
                }

                R.id.pin -> {
                    pinNote()
                    true
                }

                R.id.delete -> {
                    deleteNote()
                    true
                }
                else -> super.onContextItemSelected(it)

            }


        }

    }
//TODO: handling notes
    private fun editNoteLabel() {
       Log.i("testing", "editnotelabel() note to string ${note.toString()}")
        val action = AddEditNoteFragmentDirections.actionAddEditNoteFragmentToLabelListFragment(true,note!!.noteId)
        findNavController().navigate(action)
    }

    private fun pinNote() {
        if (note?.pinned==true){
            note?.pinned=false
        }else if (note?.pinned==false){
            note?.pinned=true
        }
        else if(note==null){
            note = Note(true)
        }
        pinCheck()
    }
    private fun loadNote(noteId: Int){
        val title = binding.titleEdittext
        val body = binding.contentEdittext
        lifecycleScope.launch{
            viewModel.getNoteById(noteId).collect {
                title.setText(it.title)
                body.setText(it.body)
                note = it
                pinCheck()
                viewModel.updatingNote(it)
            }
        }

    }

    private fun pinCheck() {
        if (note?.pinned==true){
            binding.addeditnoteTopappbar.menu[1].setIcon(R.drawable.push_pin_filled_24px)

        }else if (note?.pinned==false){
            binding.addeditnoteTopappbar.menu[1].setIcon(R.drawable.push_pin_24px)
        }
    }

    private fun saveNote(){
        if (note==null){note= Note(false)
        }
        note = Note(binding.titleEdittext.text.toString(),binding.contentEdittext.text.toString(),System.currentTimeMillis(),note!!.pinned)
        viewModel.addNote(note!!)

    }

    private fun editNote(){
        note?.title = binding.titleEdittext.text.toString()
        note?.body = binding.contentEdittext.text.toString()
        note?.timestamp = System.currentTimeMillis()
        note?.let { viewModel.updateNote(it) }
    }

    private fun deleteNote() {
        if (note!=null) {
            viewModel.deleteNote(note!!)
        }
        findNavController().popBackStack()
    }
    private fun noteValidation(){
        if (!viewModel.onEditMode()) {
            if(binding.titleEdittext.text!!.isNotEmpty()||binding.contentEdittext.text!!.isNotEmpty()){
                saveNote()
            }
        } else {
            editNote()

        }

    }



}