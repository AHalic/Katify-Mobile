package com.example.katify.view.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.example.katify.data.model.Kanban
import com.example.katify.databinding.CardKanbanBinding
import com.example.katify.view.listener.OnKanbanListener

/**
 * This class represents and individual kanban card
 *
 * Inherits [RecyclerView.ViewHolder]
 *
 * @param listener custom onClickListener
 * @param binding
 */
class KanbanViewHolder(private val binding: CardKanbanBinding, private val listener: OnKanbanListener) : RecyclerView.ViewHolder(binding.root) {

    fun bindVH(kanban: Kanban){
        binding.textKanbanCard.text = kanban.name

        binding.kanbanCard.setOnClickListener { listener.onClick(kanban) }
    }


}