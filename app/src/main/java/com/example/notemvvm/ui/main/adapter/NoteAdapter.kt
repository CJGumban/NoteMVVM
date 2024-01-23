package com.example.notemvvm.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.notemvvm.R
import com.example.notemvvm.data.Note
import com.example.notemvvm.ui.main.HomeFragmentDirections

class NoteAdapter(private val note: List<Note>) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val title: TextView
        val content: TextView

        init {
            title = view.findViewById(R.id.title_textview)
            content = view.findViewById(R.id.content_textview)

        }
    }


    override fun onCreateViewHolder(viewGroup:ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.note_row_item,viewGroup,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewholder: ViewHolder, position: Int) {
        viewholder.title.text = note[position].title
        viewholder.content.text = note[position].body
        viewholder.itemView.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToAddEditNoteFragment(note[position].noteId)
            viewholder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount() = note.size



}