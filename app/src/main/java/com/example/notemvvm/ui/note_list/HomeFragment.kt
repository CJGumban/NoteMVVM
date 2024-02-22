package com.example.notemvvm.ui.note_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notemvvm.R
import com.example.notemvvm.data.db.entities.Label
import com.example.notemvvm.data.db.entities.Note
import com.example.notemvvm.databinding.FragmentHomeBinding
import com.example.notemvvm.ui.main.adapter.NoteAdapter
import com.example.notemvvm.ui.main.adapter.NoteRecycleViewEventInterface
import com.google.android.material.search.SearchView
import com.google.android.material.search.SearchView.TransitionState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), NoteRecycleViewEventInterface {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerViewNotes: RecyclerView
    private lateinit var recyclerViewPinnedNotes: RecyclerView
    private lateinit var recyclerViewSearch: RecyclerView
    private var noteAdapter = NoteAdapter(this)
    private var pinnedNoteAdapter = NoteAdapter(this)
    private val viewModel: NoteListViewModel by viewModels()


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
        val addNotesFab = binding.addNotesFab
        val homeNavigationDrawer = binding.homeNavigationdrawer
        setupRecyclerview()
        setSearchRecyclerview()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {noteUiState->
                    Log.i(TAG,"noteUiState.collect searchmode ${noteUiState.searchMode}")
                    Log.i(TAG,"noteUiState.collect filterbyLabel${noteUiState.filterByLabelMode}")
                    Log.i(TAG,"noteUiState.collect message${noteUiState.message}")
                    Log.i(TAG,"noteUiState.collect message${noteUiState.notes}")
                    if (noteUiState.searchMode){
                        noteAdapter = NoteAdapter(this@HomeFragment)
                        recyclerViewSearch.adapter=noteAdapter
                        noteAdapter.differ.submitList(noteUiState.notes?.asReversed())
                    }else if(noteUiState.filterByLabelMode){
                        filterByLabelMode()
                        noteAdapter = NoteAdapter(this@HomeFragment)
                        recyclerViewNotes.adapter=noteAdapter
                        noteAdapter.differ.submitList(noteUiState.notes?.asReversed())
                    }
                    else{
                        val pinnedNotes: List<Note> = noteUiState.notes!!.filter { it.pinned }
                        val unpinnedNotes: List<Note> = noteUiState.notes.filter { !it.pinned }
                        setVisibility(pinnedNotes.isEmpty(),unpinnedNotes.isEmpty())
                        noteAdapter = NoteAdapter(this@HomeFragment)
                        recyclerViewNotes.adapter = noteAdapter
                        pinnedNoteAdapter = NoteAdapter(this@HomeFragment)
                        recyclerViewPinnedNotes.adapter = pinnedNoteAdapter
                        noteAdapter.differ.submitList(unpinnedNotes.asReversed())
                        pinnedNoteAdapter.differ.submitList(pinnedNotes.asReversed())
                    }
                    noteUiState.message?.let {
                        Snackbar.make(view,it,Snackbar.LENGTH_SHORT).show()
                        viewModel.snackBarShowed()
                        Log.i(TAG,"message uistate $it")
                    }
                    loadMenuDrawerLabels(noteUiState.labelItems)


                }

            }
        }
            homeNavigationDrawer.menu.findItem(R.id.add_edit_label).setOnMenuItemClickListener {
                binding.homeFragment.close()
                viewModel.onEvent(NoteListEvent.OnOffFilterByLabel)
                findNavController().navigate(R.id.action_homeFragment_to_labelListFragment)
                true
            }
            homeNavigationDrawer.menu.findItem(R.id.note_item).setOnMenuItemClickListener {
                binding.homeFragment.close()
                viewModel.onEvent(NoteListEvent.OnShowAllNotes)
                true
            }
            searchBar.setNavigationOnClickListener {
                binding.homeFragment.open()
            }
            searchView.addTransitionListener { searchView: SearchView?, previousState: TransitionState?, newState: TransitionState ->
                if (newState == TransitionState.SHOWING) {
                    // Handle search view opened.
                    viewModel.onEvent(NoteListEvent.OnSearchViewOpened)
                    setSearchRecyclerview()
                    binding.searchviewScrollbar.scrollY = 0
                } else if (newState == TransitionState.HIDING) {
                    setupRecyclerview()
                    viewModel.onEvent(NoteListEvent.OnSearchViewClosed)
                    binding.homeNestedScrollview.scrollY = 0
                }
            }
        searchView.editText.addTextChangedListener{
            viewModel.onEvent(NoteListEvent.OnSearchTextChange(it.toString()))
        }
        addNotesFab.setOnClickListener {
            viewModel.onEvent(NoteListEvent.OnOffFilterByLabel)
            findNavController().navigate(R.id.action_homeFragment_to_addEditNoteFragment)
        }
        binding.homeNavigationdrawer.setNavigationItemSelectedListener { menuItem ->
            binding.homeFragment.close()
            true
        }
        }

    private fun filterByLabelMode() {
        binding.pinnedTextview.visibility=View.GONE
        binding.pinnedNotesRecyclerView.visibility=View.GONE
        binding.unpinnedTextview.visibility=View.GONE
        binding.notesRecyclerView.visibility=View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setVisibility(isPinnedNotesEmpty: Boolean,isUnppinedNotesEmpty: Boolean){
        binding.pinnedTextview.visibility=View.VISIBLE
        binding.pinnedNotesRecyclerView.visibility=View.VISIBLE
        binding.unpinnedTextview.visibility=View.VISIBLE
        binding.notesRecyclerView.visibility=View.VISIBLE
        if (isPinnedNotesEmpty) {
            binding.pinnedTextview.visibility = View.GONE
            binding.unpinnedTextview.visibility = View.GONE
            binding.pinnedNotesRecyclerView.visibility = View.GONE
        } else if (isUnppinedNotesEmpty) {
            binding.unpinnedTextview.visibility = View.GONE
            binding.notesRecyclerView.visibility = View.GONE
        } else if (isPinnedNotesEmpty && isUnppinedNotesEmpty) {
            binding.pinnedTextview.visibility = View.GONE
            binding.unpinnedTextview.visibility = View.GONE
            binding.pinnedNotesRecyclerView.visibility = View.GONE
            binding.notesRecyclerView.visibility = View.GONE
        }
    }
    private fun loadMenuDrawerLabels(labels: List<Label>) {
        val menu = binding.homeNavigationdrawer.menu
        labels.reversed().forEach {label ->
            menu.removeItem(label.labelId)
            var menuItem = menu.add(0, label.labelId, 0, label.label)
                .setIcon(R.drawable.label_24px)
            menuItem.setOnMenuItemClickListener { menuItem->
                viewModel.onEvent(NoteListEvent.OnLabelClick(Label(menuItem.itemId,menuItem.title.toString())))
                binding.homeFragment.close()
                true
            }
        }

    }


    override fun onItemClick(noteId: Int) {
        viewModel.onEvent(NoteListEvent.OnOffFilterByLabel)
        val action = HomeFragmentDirections.actionHomeFragmentToAddEditNoteFragment(noteId)
        findNavController().navigate(action)
    }
    private fun setupRecyclerview() {

        recyclerViewNotes = binding.notesRecyclerView
        recyclerViewPinnedNotes = binding.pinnedNotesRecyclerView
        recyclerViewPinnedNotes.layoutManager = StaggeredGridLayoutManager(2, 1)
        recyclerViewNotes.layoutManager = StaggeredGridLayoutManager(2, 1)
        binding.pinnedTextview.visibility=View.VISIBLE
        binding.pinnedNotesRecyclerView.visibility=View.VISIBLE
        binding.unpinnedTextview.visibility=View.VISIBLE
        binding.notesRecyclerView.visibility=View.VISIBLE
    }
    private fun setSearchRecyclerview(){
        recyclerViewSearch = binding.notesSearchviewRecyclerview
        recyclerViewSearch.layoutManager = StaggeredGridLayoutManager(2,1)
    }

    companion object {
        const val TAG = "HomeFragment"
    }


}
