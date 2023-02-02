package com.example.katify.view.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.example.katify.data.model.Kanban
import com.example.katify.databinding.CardKanbanBinding

class KanbanViewHolder(private val binding: CardKanbanBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindVH(kanban: Kanban){
        binding.textKanbanCard.text = kanban.name
    }

}