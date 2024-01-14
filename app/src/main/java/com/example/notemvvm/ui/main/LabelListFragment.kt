package com.example.notemvvm.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isNotEmpty
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notemvvm.NoteApplication
import com.example.notemvvm.R
import com.example.notemvvm.data.Label
import com.example.notemvvm.databinding.FragmentLabelListBinding
import com.example.notemvvm.ui.main.adapter.LabelListAdapter
import com.example.notemvvm.ui.main.adapter.RecyclerViewEvent
import kotlinx.coroutines.launch


class LabelListFragment : Fragment(), RecyclerViewEvent {

    private var _binding: FragmentLabelListBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerViewLabel: RecyclerView
    private var labels:List<Label> = mutableListOf()
    private var labelAdapter = LabelListAdapter(labels,this)

    private val viewModel: AppViewModel by activityViewModels {
        AppViewModel.AppViewModelFactory(
            (activity?.application as NoteApplication).database.noteDao()
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
        showLabels()
        val addNewLabelTextField = binding.outlinedTextFieldAddnewlabel
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

    private fun showLabels() {
        recyclerViewLabel = binding.labellistRecyclerView
        recyclerViewLabel.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        lifecycleScope.launch {
            viewModel.getAllLabels().collect{
                labels = it.reversed()
                labelAdapter = LabelListAdapter(labels, this@LabelListFragment)
                recyclerViewLabel.adapter =  labelAdapter
            }

        }.invokeOnCompletion {
            Log.i("Testing", "list collect done")
        }
    }

    override fun onItemClick(position: Int) {
        val item = labels[position]
        Log.i("Testing", "fragment position title ${"$item $position"}")
        Toast.makeText(
            context,
            item.toString(),
            Toast.LENGTH_SHORT
            ).show()

    }

    override fun onStartIconClick(label: Label) {
        deleteLabel(label)
    }



    override fun onEndIconClick(label: Label) {
        updateLabel(label)
    }

    private fun updateLabel(label: Label) {

        viewModel.updateLabel(label)
            .invokeOnCompletion {
                Log.i("testing", it.toString())
                Toast.makeText(
                    context,
                    "updated: ${label.toString()}",
                    Toast.LENGTH_SHORT)
                    .show()
                showLabels()
            }
    }

    private fun deleteLabel(label: Label) {
        viewModel.deleteLabel(label)
            .invokeOnCompletion {
                Log.i("testing", it.toString())
                Toast.makeText(
                    context,
                    "deleted: ${label.toString()}",
                    Toast.LENGTH_SHORT)
                    .show()
                showLabels()
            }
    }
    private fun createLabel(){
        if (binding.outlinedTextFieldAddnewlabel.isNotEmpty()){
            var label = Label(binding.outlinedTextFieldAddnewlabel.editText!!.text.toString())
            viewModel.insertLabel(label)
                .invokeOnCompletion {
                    Log.i("testing", it.toString())
                    Toast.makeText(
                        context,
                        "Inserted: ${label.toString()}",
                        Toast.LENGTH_SHORT)
                        .show()
                    showLabels()
                }
        }
    }


}