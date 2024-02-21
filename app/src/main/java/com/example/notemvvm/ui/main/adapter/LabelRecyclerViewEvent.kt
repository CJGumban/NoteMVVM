package com.example.notemvvm.ui.main.adapter

import com.example.notemvvm.data.db.entities.Label

interface LabelRecyclerViewEvent {

    // TODO: create a function the gets a task and position
    fun onStartIconClick(label: Label)
    fun onEndIconClick(label: Label)
    fun addCrossRef(label: Label)
    fun removeCrossRef(label: Label)


}