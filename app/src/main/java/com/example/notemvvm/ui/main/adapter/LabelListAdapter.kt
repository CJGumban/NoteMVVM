package com.example.notemvvm.ui.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isNotEmpty
import androidx.recyclerview.widget.RecyclerView
import com.example.notemvvm.R
import com.example.notemvvm.data.Label
import com.google.android.material.textfield.TextInputLayout

class LabelListAdapter(
    private val label: List<Label>,
    private val listener: RecyclerViewEvent
): RecyclerView.Adapter<LabelListAdapter.ViewHolder>(){

    inner class ViewHolder(
        view: View) : RecyclerView.ViewHolder(view){


        var labelOutline: TextInputLayout

        init {

            //labelEditText = view.findViewById(R.id.label_edittext)
            labelOutline = view.findViewById(R.id.label_row_outlinedTextField)



        }

       /* override fun onClick(v: View?) {


            val position = absoluteAdapterPosition
            if (position !=RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
            Log.i("Testing", "adapter label position: $position")
        }*/

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.label_row_item,viewGroup,false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        var currentLabel = label[position]
        viewHolder.labelOutline.editText?.setText(currentLabel.label)


        viewHolder.labelOutline.editText?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                viewHolder.labelOutline.setStartIconDrawable(R.drawable.delete_24px)
                viewHolder.labelOutline.setEndIconDrawable(R.drawable.done_24px)
                viewHolder.labelOutline.setEndIconOnClickListener {
                    if (viewHolder.labelOutline.editText!!.isFocused&&viewHolder.labelOutline.isNotEmpty()){
                        Log.i("testing", "viewholder " +
                                "postition $position" +
                                "end icon pressed")
                        currentLabel = Label(currentLabel.id,viewHolder.labelOutline.editText?.text.toString())
                        listener.onEndIconClick(currentLabel)
                        viewHolder.labelOutline.editText!!.clearFocus()
                    }
                }

                viewHolder.labelOutline.setStartIconOnClickListener {
                    if (viewHolder.labelOutline.editText!!.isFocused){
                        Log.i("testing", "viewholder " +
                                "postition $position" +
                                "start icon pressed")
                        listener.onStartIconClick(currentLabel)
                        viewHolder.labelOutline.editText!!.clearFocus()

                    }
                }
            }else{
                label[position].label
                viewHolder.labelOutline.editText?.setText(currentLabel.label)
                viewHolder.labelOutline.setStartIconDrawable(R.drawable.label_24px)
                viewHolder.labelOutline.setEndIconDrawable(R.drawable.edit_24px)
                viewHolder.labelOutline.setEndIconOnClickListener(null)
                viewHolder.labelOutline.setStartIconOnClickListener(null)
            }
        }

    }



    override fun getItemCount() = label.size





}