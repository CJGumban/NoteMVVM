package com.example.notemvvm.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notemvvm.R
import com.example.notemvvm.data.db.entities.Note

class NoteAdapter(private val listener: NoteRecycleViewEventInterface) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>(){

        private val differCallback = object : DiffUtil.ItemCallback<Note>(){
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.noteId == newItem.noteId
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }
        }
    val differ = AsyncListDiffer(this,differCallback)
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
        val note = differ.currentList[position]
        viewholder.title.text = note.title
        viewholder.content.text = note.body
        viewholder.itemView.setOnClickListener {
            listener.onItemClick(note.noteId)
            /*val action = HomeFragmentDirections.actionHomeFragmentToAddEditNoteFragment(notes[position].noteId)
            viewholder.itemView.findNavController().navigate(action)*/
        }
    }
    override fun getItemCount(): Int{
        return differ.currentList.size
    }




}