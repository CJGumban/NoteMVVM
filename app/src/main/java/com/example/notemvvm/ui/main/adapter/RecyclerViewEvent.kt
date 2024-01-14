package com.example.notemvvm.ui.main.adapter

import com.example.notemvvm.data.Label

interface RecyclerViewEvent {

    // TODO: create a function the gets a task and position 
    fun onItemClick(position: Int)

    fun onStartIconClick(label: Label)
    fun onEndIconClick(label: Label)
}