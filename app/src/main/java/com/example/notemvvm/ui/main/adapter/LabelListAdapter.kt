package com.example.notemvvm.ui.main.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.view.isNotEmpty
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notemvvm.R
import com.example.notemvvm.data.db.entities.Label
import com.google.android.material.textfield.TextInputLayout


class LabelListAdapter(
    private val listener: LabelRecyclerViewEvent
): RecyclerView.Adapter<LabelListAdapter.ViewHolder>(){
    private val differCallback = object : DiffUtil.ItemCallback<Label>(){
        override fun areItemsTheSame(oldItem: Label, newItem: Label): Boolean {
            return oldItem.labelId == newItem.labelId
        }

        override fun areContentsTheSame(oldItem: Label, newItem: Label): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this,differCallback)
    inner class ViewHolder(
        view: View) : RecyclerView.ViewHolder(view){
        var labelOutline: TextInputLayout
        init {
            labelOutline = view.findViewById(R.id.label_row_outlinedTextField)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.label_row_item,viewGroup,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        var currentLabel = differ.currentList[position]
        viewHolder.labelOutline.editText?.setText(currentLabel.label)
        viewHolder.labelOutline.editText?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                viewHolder.labelOutline.setStartIconDrawable(R.drawable.delete_24px)
                viewHolder.labelOutline.setEndIconDrawable(R.drawable.done_24px)
                viewHolder.labelOutline.setEndIconOnClickListener {
                    if (viewHolder.labelOutline.editText!!.isFocused&&viewHolder.labelOutline.isNotEmpty()){
                        currentLabel = currentLabel.copy(label = viewHolder.labelOutline.editText!!.text.toString())
                        listener.onEndIconClick(currentLabel)
                        viewHolder.labelOutline.editText!!.clearFocus()
                    }
                }

                viewHolder.labelOutline.setStartIconOnClickListener {
                    if (viewHolder.labelOutline.editText!!.isFocused){
                        listener.onStartIconClick(currentLabel)
                        viewHolder.labelOutline.editText!!.clearFocus()

                    }
                }
            }else{
                viewHolder.labelOutline.editText?.setText(currentLabel.label)
                viewHolder.labelOutline.setStartIconDrawable(R.drawable.label_24px)
                viewHolder.labelOutline.setEndIconDrawable(R.drawable.edit_24px)
                viewHolder.labelOutline.setEndIconOnClickListener(null)
                viewHolder.labelOutline.setStartIconOnClickListener(null)
            }
        }

    }



    override fun getItemCount() = differ.currentList.size





}