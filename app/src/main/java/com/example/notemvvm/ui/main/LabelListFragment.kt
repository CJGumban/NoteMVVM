package com.example.notemvvm.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isNotEmpty
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notemvvm.NoteApplication
import com.example.notemvvm.R
import com.example.notemvvm.data.db.entities.Label
import com.example.notemvvm.data.db.entities.relationship.NoteLabelCrossRef
import com.example.notemvvm.databinding.FragmentLabelListBinding
import com.example.notemvvm.ui.main.adapter.CRUDLabelForNotesAdapter
import com.example.notemvvm.ui.main.adapter.CRUDLabelListAdapter
import com.example.notemvvm.ui.main.adapter.RecyclerViewEvent
import kotlinx.coroutines.launch

import kotlin.Exception


class LabelListFragment : Fragment(), RecyclerViewEvent {

    private var _binding: FragmentLabelListBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerViewLabel: RecyclerView
    private var noteLabelCrossRefs: List<NoteLabelCrossRef> = mutableListOf()
    private var labels:List<Label> = mutableListOf()
    private lateinit var crudLabelListAdapter: CRUDLabelListAdapter
    private lateinit var crudLabelForNotesAdapter: CRUDLabelForNotesAdapter
    private val args: LabelListFragmentArgs by navArgs()
    private var noteLabelCrossRefEdit: ArrayList<NoteLabelCrossRef> = arrayListOf()

    private val viewModel: AppViewModel by activityViewModels {
        AppViewModel.AppViewModelFactory(
            (activity?.application as NoteApplication).database.dao()
        )
    }


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
        if (args.noteEditMode) {
            if (args.noteId!=0){
                editNoteWithLabels(args.noteId)
            }
        }else{
            showLabels()
        }

       Log.i("testing","args ${args.toString()}")

        val addNewLabelTextField = binding.outlinedTextFieldAddnewlabel
        binding.labelListTopappbar.setNavigationOnClickListener {
            if (args.noteEditMode){
                lifecycleScope.launch {
                    viewModel.deleteNoteLabelCrossRefByNoteId(args.noteId)
                    noteLabelCrossRefEdit.forEach {
                        viewModel.addNoteLabelCrossRef(it)
                    }
                    findNavController().navigateUp()
                }
            }else{ findNavController().navigateUp()}

        }
        try {

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
        } catch (e: Exception){
            Log.i("testing", "error ${e.toString()}")
        }
    }

    private fun editNoteWithLabels(noteId: Int) {

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
/*
                noteLabelCrossRefs = it.reversed().filter { noteLabelCrossRefs-> noteLabelCrossRefs.noteId==noteId  }
*/
                noteLabelCrossRefs=it
                Log.i("testing",
                    "notelabelscrossref ${noteLabelCrossRefs.toString()}"
                )

                crudLabelForNotesAdapter = CRUDLabelForNotesAdapter(labels, noteLabelCrossRefs,noteId, this@LabelListFragment)
                recyclerViewLabel.adapter =  crudLabelForNotesAdapter

            }
        }

    }

    private fun showLabels() {


        lifecycleScope.launch {
            viewModel.getAllLabels().collect{
                labels = it.reversed()
                recyclerViewLabel = binding.labellistRecyclerView
                recyclerViewLabel.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                crudLabelListAdapter = CRUDLabelListAdapter(labels, this@LabelListFragment)
                recyclerViewLabel.adapter =  crudLabelListAdapter
            }

        }
    }

    override fun addCrossRef(crossRef: NoteLabelCrossRef) {
        noteLabelCrossRefEdit.add(crossRef)

    }
    override fun removeCrossRef(noteLabelCrossRef: NoteLabelCrossRef){
        noteLabelCrossRefEdit.remove(noteLabelCrossRef)
    }

    override fun onItemClick(position: Int) {}

    override fun onStartIconClick(label: Label) {
        deleteLabel(label)
    }



    override fun onEndIconClick(label: Label) {
        updateLabel(label)
    }

    private fun updateLabel(label: Label) {

        viewModel.updateLabel(label)
            .invokeOnCompletion {
                showLabels()
            }
    }

    private fun deleteLabel(label: Label) {
        viewModel.deleteLabel(label)
            .invokeOnCompletion {
                showLabels()
            }
    }
    private fun createLabel(){
        if (binding.outlinedTextFieldAddnewlabel.isNotEmpty()){
            var label = Label(binding.outlinedTextFieldAddnewlabel.editText!!.text.toString())
            viewModel.insertLabel(label)
                .invokeOnCompletion {

                    showLabels()
                }
        }
    }


}