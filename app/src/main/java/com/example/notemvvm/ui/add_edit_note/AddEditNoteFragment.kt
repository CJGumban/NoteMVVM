package com.example.notemvvm.ui.add_edit_note

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notemvvm.R
import com.example.notemvvm.data.db.entities.Note
import com.example.notemvvm.databinding.FragmentAddEditNoteBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditNoteFragment : Fragment() {
    private var _binding: FragmentAddEditNoteBinding? = null
    private val args: AddEditNoteFragmentArgs by navArgs()
    lateinit var pinButton: MenuItem
    private var isPinned = false
    private val binding get() = _binding!!
    private val viewModel: AddEditNoteViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val titleEditText = binding.titleEdittext
        val contentEditText = binding.contentEdittext
        pinButton = binding.addeditnoteTopappbar.menu[1]
        viewModel.viewModelScope.launch { viewModel.start(args.noteId) }


        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    titleEditText.setText(uiState.note.title)
                    contentEditText.setText(uiState.note.body)
                    isPinned = uiState.note.pinned
                    loadPinIcon()
                }
            }
        }
        binding.addeditnoteTopappbar.setNavigationOnClickListener {
            val note = Note(
                args.noteId,
                titleEditText.text.toString(),
                contentEditText.text.toString(),
                System.currentTimeMillis(),
                isPinned)
            Log.i(TAG,"${note}")
            viewModel.viewModelScope.launch {
              Log.i(TAG, "viewmodel saved NoteId ${viewModel.saveNote(note)}")
                view.findNavController().navigateUp()
            }
        }

        binding.addeditnoteTopappbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.label -> {
                   editNoteLabel()
                    true
                }

                R.id.pin -> {
                    pinClick()
                    true
                }

                R.id.delete -> {
                    viewModel.viewModelScope.launch {
                        viewModel.deleteNote()
                    }.invokeOnCompletion { findNavController().navigateUp() }


                    true
                }
                else -> super.onContextItemSelected(it)

            }


        }

    }

    private fun loadPinIcon() {
        val pinned = R.drawable.push_pin_filled_24px
        val unpinned = R.drawable.push_pin_24px
         if (isPinned){
         pinButton.setIcon(pinned)
        }else {
            pinButton.setIcon(unpinned)
        }
    }
    private fun pinClick() {
        isPinned=!isPinned
        loadPinIcon()
    }

    private fun editNoteLabel() {
        val note = Note(
            args.noteId,
            binding.titleEdittext.text.toString(),
            binding.contentEdittext.text.toString(),
            System.currentTimeMillis(),
            isPinned)
        Log.i(TAG,"${note}")

        if (note.noteId==0){
            if (note.title.isEmpty()&&note.body.isEmpty()){
                Snackbar.make(requireView(),"Do not leave the Title and Note empty.",Snackbar.LENGTH_SHORT).show()
            }else{
                viewModel.viewModelScope.launch {
                val noteId = viewModel.saveNote(note)
                val action = AddEditNoteFragmentDirections.actionAddEditNoteFragmentToLabelListFragment(
                    noteId)
                findNavController().navigate(action)
            }}

        }else{
            viewModel.viewModelScope.launch {
                viewModel.saveNote(note)
                val action = AddEditNoteFragmentDirections.actionAddEditNoteFragmentToLabelListFragment(
                    args.noteId)
                findNavController().navigate(action)
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }





companion object{
    const val TAG = "AddEditNoteFragment"
}

}