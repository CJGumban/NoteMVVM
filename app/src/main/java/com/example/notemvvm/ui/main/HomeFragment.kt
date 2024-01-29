package com.example.notemvvm.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
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
import com.example.notemvvm.data.db.entities.Label
import com.example.notemvvm.data.db.entities.Note
import com.example.notemvvm.databinding.FragmentHomeBinding
import com.example.notemvvm.ui.main.adapter.NoteAdapter
import com.google.android.material.search.SearchView
import com.google.android.material.search.SearchView.TransitionState
import kotlinx.coroutines.launch

// TODO: create editmode on viewmodel 
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerViewNotes: RecyclerView
    private lateinit var recyclerViewPinnedNotes: RecyclerView
    private lateinit var recyclerViewSearch: RecyclerView
    private var notes:List<Note> = mutableListOf()
    private var labels: List<Label> = mutableListOf()
    private var noteAdapter = NoteAdapter(notes)
    private var pinnedNoteAdapter = NoteAdapter(notes)
    private var noteSearchAdapter = NoteAdapter(notes)
    val TAG = "HomeFragment"
    private val viewModel: AppViewModel by activityViewModels{
        AppViewModel.AppViewModelFactory(
            (activity?.application as NoteApplication).database.dao()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchBar = binding.searchBar
        val searchView = binding.notesSearchview
        val addNotes = binding.addNotesFab
        val homeNavigationDrawer = binding.homeNavigationdrawer
        loadMenuDrawerLabels()
        loadNotes()

        homeNavigationDrawer.menu.findItem(R.id.add_edit_label).setOnMenuItemClickListener {
            binding.homeFragment.close()
            view.findNavController().navigate(R.id.action_homeFragment_to_labelListFragment)
            true
        }
        homeNavigationDrawer.menu.findItem(R.id.note_item).setOnMenuItemClickListener {
            binding.homeFragment.close()
            loadNotes()
            true
        }
        searchBar.setNavigationOnClickListener {
            binding.homeFragment.open()
        }


        searchView.addTransitionListener { searchView: SearchView?, previousState: TransitionState?, newState: TransitionState ->
            if (newState == TransitionState.SHOWING) {
                // Handle search view opened.
                binding.searchviewScrollbar.scrollY = 0
            }else if (newState == TransitionState.HIDING){
                binding.homeNestedScrollview.scrollY = 0
            }
        }

        searchView.editText.addTextChangedListener{
            searchNotes(it.toString())
        }
        addNotes.setOnClickListener {
            view.findNavController().navigate(R.id.action_homeFragment_to_addEditNoteFragment)
        }
        binding.homeNavigationdrawer.setNavigationItemSelectedListener { menuItem->
            binding.homeFragment.close()
            true
        }
    }

    private fun searchNotes(text:String = "") {
        recyclerViewSearch = binding.notesSearchviewRecyclerview
        recyclerViewSearch.layoutManager = StaggeredGridLayoutManager(2, 1)
        lifecycleScope.launch {
            viewModel.searchNotesByText(text).collect {
                notes = it.reversed()
                Log.i(TAG, it.toString())
                noteSearchAdapter = NoteAdapter(notes)
                recyclerViewSearch.adapter = noteSearchAdapter
            }
        }
    }
    private fun loadNotes() {

        //gets data from the database and populate the recyclerView
        var pinnednotes: List<Note> = mutableListOf()
        var unpinnednotes: List<Note> = mutableListOf()
        recyclerViewNotes = binding.notesRecyclerView
        recyclerViewPinnedNotes = binding.pinnedNotesRecyclerView
        recyclerViewPinnedNotes.layoutManager = StaggeredGridLayoutManager(2, 1)
        recyclerViewNotes.layoutManager = StaggeredGridLayoutManager(2, 1)
        lifecycleScope.launch {
            viewModel.allNotes.collect{
                notes = it.reversed()
                pinnednotes = notes.filter { it.pinned }
                unpinnednotes = notes.filter { !it.pinned }

                binding.pinnedTextview.visibility=View.VISIBLE
                binding.pinnedNotesRecyclerView.visibility=View.VISIBLE
                binding.unpinnedTextview.visibility=View.VISIBLE
                if (pinnednotes.isEmpty()){
                    binding.pinnedTextview.visibility=View.GONE
                    binding.unpinnedTextview.visibility=View.GONE
                    binding.pinnedNotesRecyclerView.visibility=View.GONE
                }else if (unpinnednotes.isEmpty()){
                    binding.unpinnedTextview.visibility=View.GONE
                    binding.notesRecyclerView.visibility=View.GONE
                }else if (pinnednotes.isEmpty()&&unpinnednotes.isEmpty()) {
                    binding.pinnedTextview.visibility=View.GONE
                    binding.unpinnedTextview.visibility=View.GONE
                    binding.pinnedNotesRecyclerView.visibility=View.GONE
                    binding.notesRecyclerView.visibility=View.GONE
                }
                Log.i(TAG,"pinned notes list ${pinnednotes.isEmpty()}" )
                noteAdapter = NoteAdapter(unpinnednotes)
                recyclerViewNotes.adapter = noteAdapter
                pinnedNoteAdapter = NoteAdapter(pinnednotes)
                recyclerViewPinnedNotes.adapter = pinnedNoteAdapter

            }

        }
    }
    private fun loadMenuDrawerLabels() {
        lifecycleScope.launch {
            viewModel.getAllLabels().collect {
                labels = it.reversed()
                labels.forEach {label ->
                    var m = binding.homeNavigationdrawer.menu.add(0, label.labelId, 0, label.label)
                        .setIcon(R.drawable.label_24px)
                    Log.i(TAG, "menu $m")
                    m.setOnMenuItemClickListener {menuItem->
                        Log.i(
                            TAG, "menu ${menuItem.groupId}" +
                                    "${menuItem.itemId}" +
                                    "${menuItem.title}"
                        )
                        filterNotesByLabel(menuItem.itemId)
                        binding.homeFragment.close()
                        true
                    }
                }
            }
        }
    }
//this line of code gets labelId for filtering notes
fun filterNotesByLabel(labelId: Int){
     var filteredByLabel: List<Note>
     lifecycleScope.launch {
         viewModel.filterNotesByLabel(labelId).collect { it ->

             filteredByLabel=it
             binding.pinnedTextview.visibility=View.GONE
             binding.unpinnedTextview.visibility=View.GONE
             binding.pinnedNotesRecyclerView.visibility=View.GONE
             noteAdapter = NoteAdapter(filteredByLabel)
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
