package com.example.notemvvm.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.notemvvm.NoteApplication
import com.example.notemvvm.R
import com.example.notemvvm.databinding.FragmentAddEditNoteBinding

class AddEditNoteFragment : Fragment() {
    private var _binding: FragmentAddEditNoteBinding? = null
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
        return inflater.inflate(R.layout.fragment_add_edit_note, container, false)
    }




}