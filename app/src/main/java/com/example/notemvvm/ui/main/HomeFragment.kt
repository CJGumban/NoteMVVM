package com.example.notemvvm.ui.main

import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import androidx.core.view.forEach
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notemvvm.NoteApplication
import com.example.notemvvm.R
import com.example.notemvvm.data.Label
import com.example.notemvvm.data.Note
import com.example.notemvvm.databinding.FragmentHomeBinding
import com.example.notemvvm.ui.main.adapter.NoteAdapter
import com.example.notemvvm.ui.main.adapter.NoteSearchAdapter
import kotlinx.coroutines.launch

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchBar = binding.searchBar
        val searchView = binding.notesSearchview
        var actionMode: ActionMode? = null
        val addNotes = binding.addNotesFab
        loadLabels()
        loadNotes()





        binding.homeNavigationdrawer.menu.findItem(R.id.add_edit_label).setOnMenuItemClickListener {
            binding.homeFragment.close()
            view.findNavController().navigate(R.id.action_homeFragment_to_labelListFragment)
            true
        }
        binding.homeNavigationdrawer.menu.findItem(R.id.note_item).setOnMenuItemClickListener {
            binding.homeFragment.close()
            loadNotes()
            true
        }




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
            binding.homeFragment.open()
        }
        binding.searchviewScrollbar.setOnScrollChangeListener{ _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY){
                searchView.clearFocusAndHideKeyboard()
                searchView.editText.clearFocus()
            }
            binding.homeNestedScrollview.scrollY = 0
            searchBar.collapse(searchBar,binding.appBarLayout,true)
        }
        binding.homeNavigationdrawer.setNavigationItemSelectedListener { menuItem->
            menuItem.isChecked = true

            binding.homeFragment.close()
            true
        }
        // TODO: pop up to pick label

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    private fun loadSearchNotes(text:String = "") {
        recyclerViewSearch = binding.notesSearchviewRecyclerview
        recyclerViewSearch.layoutManager = StaggeredGridLayoutManager(2, 1)
        lifecycleScope.launch {

            viewModel.searchNotesByText(text).collect {
                notes = it.reversed()
                Log.i("homefragment", it.toString())
                noteSearchAdapter = NoteSearchAdapter(notes)
                recyclerViewSearch.adapter = noteSearchAdapter
            }
        }



    }
    private fun loadNotes() {
        //gets data from the database and populate the recyclerView
        var pinnednotes: List<Note>
        var unpinnednotes: List<Note>
        recyclerViewNotes = binding.notesRecyclerView
        recyclerViewPinnedNotes = binding.pinnedNotesRecyclerView
        recyclerViewPinnedNotes.layoutManager = StaggeredGridLayoutManager(2, 1)
        recyclerViewNotes.layoutManager = StaggeredGridLayoutManager(2, 1)
        lifecycleScope.launch {
            viewModel.allNotes.collect{
                notes = it.reversed()
                pinnednotes = notes.filter { it.pinned }
                unpinnednotes = notes.filter { !it.pinned }
                Log.i("HomeFragment","pinned notes list $pinnednotes" )
                Log.i("HomeFragment", "unpinned notes list $unpinnednotes")
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
                noteAdapter = NoteAdapter(unpinnednotes)
                recyclerViewNotes.adapter = noteAdapter
                pinnedNoteAdapter = NoteAdapter(pinnednotes)
                recyclerViewPinnedNotes.adapter = pinnedNoteAdapter
            }

        }
    }

    fun loadLabels(){

        lifecycleScope.launch {
            viewModel.getAllLabels().collect{
                labels = it.reversed()
                labels.forEach{
                    var m = binding.homeNavigationdrawer.menu.add(0,it.id,0,it.label)
                        .setIcon(R.drawable.label_24px)
                    Log.i("testing", "menu ${m}")
                    m.setOnMenuItemClickListener {
                        Log.i("testing", "menu ${it.groupId}" +
                                "${it.itemId}" +
                                "${it.title}" )
                        filterNotesByLabel(it.title.toString())
                        true
                    }

                }
                }
            }
        binding.homeNavigationdrawer.menu.forEach {
            Log.i("testing", "${it}")
            it.setOnMenuItemClickListener {
                Log.i("testing", "${it} clicked")
                true
            }
        }

    }

    private fun filterNotesByLabel(title: String) {
        var filteredByLabel: List<Note> = notes?.filter {
            it.label==title
        }!!
        binding.pinnedTextview.visibility=View.GONE
        binding.unpinnedTextview.visibility=View.GONE
        binding.pinnedNotesRecyclerView.visibility=View.GONE
        noteAdapter = NoteAdapter(filteredByLabel)
        recyclerViewNotes.adapter = noteAdapter
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
