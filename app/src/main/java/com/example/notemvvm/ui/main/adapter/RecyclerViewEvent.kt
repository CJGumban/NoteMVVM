package com.example.notemvvm.ui.main.adapter

import com.example.notemvvm.data.Label
import com.example.notemvvm.data.relationship.NoteLabelCrossRef

interface RecyclerViewEvent {

    // TODO: create a function the gets a task and position 
    fun onItemClick(position: Int)

    fun onStartIconClick(label: Label)
    fun onEndIconClick(label: Label)
    fun addCrossRef(crossRef: NoteLabelCrossRef)
    fun removeCrossRef(crossRef: NoteLabelCrossRef)


}