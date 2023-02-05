package com.example.katify.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.katify.data.model.Note
import com.example.katify.databinding.CardNoteBinding
import com.example.katify.view.listener.OnNoteListener
import com.example.katify.view.viewHolder.NoteViewHolder

class NoteAdapter : RecyclerView.Adapter<NoteViewHolder>() {
    var noteList: List<Note> = listOf()
    private lateinit var listener: OnNoteListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val item = CardNoteBinding.inflate(LayoutInflater.from(parent.context),
            parent,false)
        return NoteViewHolder(item, listener)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bindVH(noteList[position])
    }

    override fun getItemCount(): Int {
        return noteList.count()
    }

    fun updateNoteList(list: List<Note>) {
        noteList = list
        notifyDataSetChanged()
    }

    fun setListener(noteListener: OnNoteListener) {
        listener = noteListener
    }

}