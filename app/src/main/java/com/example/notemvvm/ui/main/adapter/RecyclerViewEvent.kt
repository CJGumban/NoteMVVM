package com.example.notemvvm.ui.main.adapter

interface RecyclerViewEvent {

    // TODO: create a function the gets a task and position 
    fun onItemClick(position: Int)

    fun onStartIconClick(position: Int)
    fun onEndIconClick(position: Int)
}