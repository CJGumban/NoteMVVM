package com.example.notemvvm.ui.main.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.notemvvm.R
import com.example.notemvvm.data.Label
import com.example.notemvvm.data.relationship.NoteLabelCrossRef
import com.google.android.material.textfield.TextInputLayout

class CRUDLabelForNotesAdapter (
    private val labels: List<Label>,
    private val noteLabelCrossRefs: List<NoteLabelCrossRef>,
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
        var check = false
        var count = 0
        viewHolder.labelOutline.editText?.setText(currentLabel.label)
        viewHolder.labelOutline.editText?.isFocusable=false
        while (!check&&noteLabelCrossRefs.size!=count){
            check=currentLabel.labelId==noteLabelCrossRefs[count].labelId
            count++
        }
        if (check){
            viewHolder.labelOutline.setEndIconDrawable(R.drawable.check_box_24px)
        }else{viewHolder.labelOutline.setEndIconDrawable(R.drawable.check_box_outline_blank_24px)
        }
        viewHolder.labelOutline.editText?.setOnClickListener {
            if (check){
                listener.removeCrossRef(noteLabelCrossRefs[count])

            }else{
                listener.addCrossRef(currentLabel.labelId)
            }
        }


    }



    override fun getItemCount() = labels.size


}