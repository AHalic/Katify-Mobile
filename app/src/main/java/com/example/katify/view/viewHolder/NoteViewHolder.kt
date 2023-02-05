package com.example.katify.view.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.example.katify.data.model.Note
import com.example.katify.databinding.CardNoteBinding
import com.example.katify.view.listener.OnNoteListener
import kotlin.math.min

class NoteViewHolder(private val binding: CardNoteBinding, private val listener: OnNoteListener) : RecyclerView.ViewHolder(binding.root) {

    fun bindVH(note: Note){
        binding.nameNoteCard.text = note.note_name
        var endText = min(note.content.length, 20)
        binding.descrptNoteCard.text = note.content.subSequence(0, endText)

        binding.noteCard.setOnClickListener { listener.onClick(note) }
    }


}