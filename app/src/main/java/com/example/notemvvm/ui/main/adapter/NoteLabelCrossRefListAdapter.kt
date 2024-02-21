package com.example.notemvvm.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notemvvm.R
import com.example.notemvvm.ui.label_list.NoteLabelCrossRefItem
import com.google.android.material.textfield.TextInputLayout

class NoteLabelCrossRefListAdapter (
    private val listener: LabelRecyclerViewEvent
): RecyclerView.Adapter<NoteLabelCrossRefListAdapter.ViewHolder>(){

    private val differCallback = object : DiffUtil.ItemCallback<NoteLabelCrossRefItem>(){
        override fun areItemsTheSame(
            oldItem: NoteLabelCrossRefItem,
            newItem: NoteLabelCrossRefItem,
        ): Boolean {
            return oldItem.label.labelId == newItem.label.labelId
        }

        override fun areContentsTheSame(
            oldItem: NoteLabelCrossRefItem,
            newItem: NoteLabelCrossRefItem,
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallback)
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
        val currentLabel = differ.currentList[position]

        viewHolder.labelOutline.editText?.setText(currentLabel.label.label.toString())
        viewHolder.labelOutline.editText?.isFocusable=false


        if (currentLabel.isChecked){
            viewHolder.labelOutline.setEndIconDrawable(R.drawable.check_box_24px)
        }else{viewHolder.labelOutline.setEndIconDrawable(R.drawable.check_box_outline_blank_24px)
        }
        viewHolder.labelOutline.editText?.setOnClickListener {
            if (currentLabel.isChecked){
                listener.removeCrossRef(currentLabel.label)
            }else{
                listener.addCrossRef(currentLabel.label)
            }
        }


    }



    override fun getItemCount() = differ.currentList.size


}