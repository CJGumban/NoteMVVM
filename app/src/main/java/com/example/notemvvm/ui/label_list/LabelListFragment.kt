package com.example.notemvvm.ui.label_list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isNotEmpty
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notemvvm.R
import com.example.notemvvm.data.db.entities.Label
import com.example.notemvvm.data.db.entities.relationship.NoteLabelCrossRef
import com.example.notemvvm.databinding.FragmentLabelListBinding
import com.example.notemvvm.ui.main.adapter.NoteLabelCrossRefListAdapter
import com.example.notemvvm.ui.main.adapter.LabelListAdapter
import com.example.notemvvm.ui.main.adapter.LabelRecyclerViewEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

import kotlin.Exception

@AndroidEntryPoint

class LabelListFragment : Fragment(), LabelRecyclerViewEvent {

    private var _binding: FragmentLabelListBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerViewLabel: RecyclerView
    private lateinit var labelListAdapter: LabelListAdapter
    private lateinit var noteLabelCrossRefListAdapter: NoteLabelCrossRefListAdapter
    private val args: LabelListFragmentArgs by navArgs()
    private var noteLabelCrossRefEdit: ArrayList<NoteLabelCrossRef> = arrayListOf()
    private val viewModel: LabelListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLabelListBinding.inflate(inflater, container, false)
        return binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.start(args.noteId)
        val addNewLabelTextField = binding.outlinedTextFieldAddnewlabel
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect{
                    uiState->
                    Log.i(TAG,"${uiState}")

                    if (uiState.addEditLabelMode){
                        recyclerViewLabel = binding.labellistRecyclerView
                        recyclerViewLabel.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                        labelListAdapter = LabelListAdapter(this@LabelListFragment)
                        recyclerViewLabel.adapter =  labelListAdapter
                        labelListAdapter.differ.submitList(uiState.labelItems.reversed())

                    }else if (!uiState.addEditLabelMode){
                        recyclerViewLabel = binding.labellistRecyclerView
                        recyclerViewLabel.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                        noteLabelCrossRefListAdapter = NoteLabelCrossRefListAdapter(this@LabelListFragment)
                        recyclerViewLabel.adapter =  noteLabelCrossRefListAdapter
                        noteLabelCrossRefListAdapter.differ.submitList(uiState.noteLabelsCrossRefItems.reversed())
                    }
                }
            }
        }




        binding.labelListTopappbar.setNavigationOnClickListener {
            findNavController().navigateUp()

        }


        addNewLabelTextField.editText?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                addNewLabelTextField.setStartIconDrawable(R.drawable.close_24px)
                addNewLabelTextField.setEndIconDrawable(R.drawable.done_24px)
                addNewLabelTextField.isEndIconVisible = true
                addNewLabelTextField.setEndIconOnClickListener {
                    if (addNewLabelTextField.editText!!.isFocused&&addNewLabelTextField.isNotEmpty()){
                        Log.i("testing","end icon pressed")
                        createLabel()
                        addNewLabelTextField.clearFocus()

                    }
                }
                addNewLabelTextField.setStartIconOnClickListener {
                        Log.i("testing","start icon pressed")
                        addNewLabelTextField.editText!!.setText("")
                    addNewLabelTextField.clearFocus()
                }

            }else{
                addNewLabelTextField.editText!!.setText("")
                addNewLabelTextField.isEndIconVisible = false
                addNewLabelTextField.isStartIconVisible = false
                addNewLabelTextField.isStartIconVisible = true
                addNewLabelTextField.setStartIconDrawable(R.drawable.add_24px)
                addNewLabelTextField.setEndIconOnClickListener(null)
                addNewLabelTextField.setStartIconOnClickListener(null)
            }
        }
    }

    /*private fun editNoteWithLabels(noteId: Int) {

        recyclerViewLabel = binding.labellistRecyclerView
        recyclerViewLabel.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        lifecycleScope.launch {
            viewModel.getAllLabels().collect {
                labels = it.reversed()
                Log.i("testing","labels: ${labels.toString()}")
            }
            Log.i("testing","labels: ${labels.toString()}" +
                    "" +
                    "notelabelscrossref ${noteLabelCrossRefs.toString()}")

        }
        lifecycleScope.launch {
            viewModel.getAllNoteLabelCrossRef().collect{
                noteLabelCrossRefs.forEach {
                    noteLabelCrossRefEdit.add(it)
                }

                noteLabelCrossRefs = it.reversed().filter { noteLabelCrossRefs-> noteLabelCrossRefs.noteId==noteId  }

                noteLabelCrossRefs=it
                Log.i("testing",
                    "notelabelscrossref ${noteLabelCrossRefs.toString()}"
                )

                crudLabelForNotesAdapter = CRUDLabelForNotesAdapter(labels, noteLabelCrossRefs,noteId, this@LabelListFragment)
                recyclerViewLabel.adapter =  crudLabelForNotesAdapter

            }
        }

    }*/

    override fun addCrossRef(label: Label) {
        viewModel.insertNoteLabelCrossRef(NoteLabelCrossRef(args.noteId,label.labelId))
    }
    override fun removeCrossRef(label: Label){
        viewModel.deleteNoteLabelCrossRef(NoteLabelCrossRef(args.noteId,label.labelId))
    }

    override fun onStartIconClick(label: Label) {
        deleteLabel(label)
    }

    override fun onEndIconClick(label: Label) {
        updateLabel(label)
    }

    private fun updateLabel(label: Label) {
        viewModel.updateLabel(label)
    }

    private fun deleteLabel(label: Label) {
        viewModel.deleteLabel(label)
    }
    private fun createLabel(){
        if (binding.outlinedTextFieldAddnewlabel.isNotEmpty()){
            var label = Label(binding.outlinedTextFieldAddnewlabel.editText!!.text.toString())
            viewModel.insertLabel(label)
        }
    }

    companion object {
        const val TAG = "LabelListFragment"
    }


}