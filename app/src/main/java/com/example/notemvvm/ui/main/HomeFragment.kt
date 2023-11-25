package com.example.notemvvm.ui.main

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.room.util.query
import com.example.notemvvm.NoteApplication
import com.example.notemvvm.R
import com.example.notemvvm.data.Note
import com.example.notemvvm.databinding.FragmentHomeBinding
import com.example.notemvvm.ui.main.adapter.NoteAdapter
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.random.Random

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    var notes:List<Note> = mutableListOf()
    var noteAdapter = NoteAdapter(notes)
    private val viewModel: HomeViewModel by activityViewModels{
        HomeViewModel.HomeViewModelFactory(
            (activity?.application as NoteApplication).database.noteDao()
        )
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchBar = binding.searchBar
        var actionMode: ActionMode? = null
        /*
        actionmode code for multiple item selection
        val callback = object : ActionMode.Callback {

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode?.menuInflater?.inflate(R.menu.home_top_app_bar_menu,menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return when (item?.itemId) {
                    R.id.label -> {
                        // Handle share icon press

                        mode?.finish()
                        searchBar.visibility=(View.VISIBLE)


                        true
                    }
                    R.id.pin -> {
                        // Handle delete icon press

                        true
                    }
                    R.id.delete -> {
                        // Handle more item (inside overflow menu) press
                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode?) {

            }

        }
        actionMode = activity?.startActionMode(callback)
        actionMode?.title = "1 selected"*/
        showNotes()

        searchBar.setNavigationOnClickListener {
            Log.i("codelog_homefragment","searchBarnav clicked")
        }





    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showNotes(){
        //gets data from the database and populate the recyclerView
        recyclerView = binding.notesRecyclerView
        recyclerView.layoutManager = StaggeredGridLayoutManager(2,1)
        lifecycleScope.launch {
            viewModel.allNotes.collect(){
                notes = it
                Log.i("homefragment", it.toString())
                noteAdapter = NoteAdapter(notes)
                recyclerView.adapter = noteAdapter
            }
        }
    }

    //tested recycler view with random content
    /*fun generateLetters():String{
        val length = Random.nextInt(1, 500) // Generate a random length between 1 and 120
        val letters = CharArray(length) {
            ('a'..'z').random() // Generate a random lowercase letter
        }
        return String(letters)
    }*/

}