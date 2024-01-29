package com.example.notemvvm.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notemvvm.R
import com.example.notemvvm.data.db.entities.Label
import com.example.notemvvm.data.db.entities.relationship.NoteLabelCrossRef
import com.google.android.material.textfield.TextInputLayout

class CRUDLabelForNotesAdapter (
    private val labels: List<Label>,
    private val noteLabelCrossRefs: List<NoteLabelCrossRef>,
    private val noteId:Int,
    private val listener: RecyclerViewEvent
): RecyclerView.Adapter<CRUDLabelForNotesAdapter.ViewHolder>(){

    inner class ViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view){
        var labelOutline: TextInputLayout
        init {
            labelOutline = view.findViewById(R.id.label_row_button)

        }



    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.label_attach_note_row_item,viewGroup,false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        var currentLabel = labels[position]
        var check = noteLabelCrossRefs.contains(NoteLabelCrossRef(noteId,currentLabel.labelId))
        viewHolder.labelOutline.editText?.setText(currentLabel.label)
        viewHolder.labelOutline.editText?.isFocusable=false


        if (check){
            viewHolder.labelOutline.setEndIconDrawable(R.drawable.check_box_24px)
        }else{viewHolder.labelOutline.setEndIconDrawable(R.drawable.check_box_outline_blank_24px)
        }
        viewHolder.labelOutline.editText?.setOnClickListener {
            if (check){
                listener.removeCrossRef(NoteLabelCrossRef(noteId,currentLabel.labelId))
                viewHolder.labelOutline.setEndIconDrawable(R.drawable.check_box_outline_blank_24px)
                check=false

            }else{
                listener.addCrossRef(NoteLabelCrossRef(noteId,currentLabel.labelId))
                viewHolder.labelOutline.setEndIconDrawable(R.drawable.check_box_24px)
                check=true
            }
        }


    }



    override fun getItemCount() = labels.size


}