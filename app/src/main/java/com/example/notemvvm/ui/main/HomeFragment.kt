package com.example.notemvvm.ui.main

import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notemvvm.NoteApplication
import com.example.notemvvm.R
import com.example.notemvvm.data.Note
import com.example.notemvvm.databinding.FragmentHomeBinding
import com.example.notemvvm.ui.main.adapter.NoteAdapter
import com.example.notemvvm.ui.main.adapter.NoteSearchAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerViewNotes: RecyclerView
 //   private lateinit var recyclerViewPinnedNotes: RecyclerView
    private lateinit var recyclerViewSearch: RecyclerView
    private var notes:List<Note> = mutableListOf()
    private var noteAdapter = NoteAdapter(notes)
    private var noteSearchAdapter = NoteSearchAdapter(notes)
    private val viewModel: AppViewModel by activityViewModels{
        AppViewModel.AppViewModelFactory(
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
        val searchView = binding.notesSearchview
        var actionMode: ActionMode? = null
        val addNotes = binding.addNotesFab

        showNotes()
        val callback = object : ActionMode.Callback {

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode?.menuInflater?.inflate(R.menu.home_fragment_action_bar_menu,menu)

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
                binding.appBarLayout.visibility = View.VISIBLE
            }

        }



        binding.homeNestedScrollview.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            binding.searchviewScrollbar.scrollY = 0
        }
        searchView.editText.addTextChangedListener{
            loadSearchNotes(it.toString())
        }
        
        addNotes.setOnClickListener {
            view.findNavController().navigate(R.id.action_homeFragment_to_addEditNoteFragment)
        }

        searchBar.setNavigationOnClickListener {

        }
        binding.searchviewScrollbar.setOnScrollChangeListener{ _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY){
                searchView.clearFocusAndHideKeyboard()
                searchView.editText.clearFocus()
            }
            binding.homeNestedScrollview.scrollY = 0
            searchBar.collapse(searchBar,binding.appBarLayout,true)
        }
    }



    private fun loadSearchNotes(text:String = "") {
        recyclerViewSearch = binding.notesSearchviewRecyclerview
        recyclerViewSearch.layoutManager = StaggeredGridLayoutManager(2, 1)
        lifecycleScope.launch {

            viewModel.searchNotesByText(text).collect {
                notes = it
                Log.i("homefragment", it.toString())
                noteSearchAdapter = NoteSearchAdapter(notes)
                recyclerViewSearch.adapter = noteSearchAdapter
            }
        }



    }
    private fun showNotes() {
        //gets data from the database and populate the recyclerView
        recyclerViewNotes = binding.notesRecyclerView
/*
        recyclerViewPinnedNotes = binding.pinnedNotesRecyclerView
*/
/*
        recyclerViewPinnedNotes.layoutManager = StaggeredGridLayoutManager(2, 1)
*/
        recyclerViewNotes.layoutManager = StaggeredGridLayoutManager(2, 1)
        lifecycleScope.launch {
            viewModel.allNotes.collect{
                notes = it
       /*         var pinnedNotes = notes.filter { note->
                    note.pinned
                }*/
                notes = notes.filter { note ->
                    note.pinned
                }
                /*Log.i("homefragment", pinnedNotes.toString())*/
                noteAdapter = NoteAdapter(notes)
                recyclerViewNotes.adapter = noteAdapter

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
