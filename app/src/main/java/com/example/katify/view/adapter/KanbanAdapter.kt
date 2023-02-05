package com.example.katify.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.katify.data.model.Kanban
import com.example.katify.databinding.CardKanbanBinding
import com.example.katify.view.listener.OnKanbanListener
import com.example.katify.view.viewHolder.KanbanViewHolder

class KanbanAdapter : RecyclerView.Adapter<KanbanViewHolder>() {

    var kanbanList: List<Kanban> = listOf()
    private lateinit var listener: OnKanbanListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KanbanViewHolder {
        val item = CardKanbanBinding.inflate(LayoutInflater.from(parent.context),
            parent,false)
        return KanbanViewHolder(item, listener)
    }

    override fun onBindViewHolder(holder: KanbanViewHolder, position: Int) {
        holder.bindVH(kanbanList[position])
    }

    override fun getItemCount(): Int {
        return kanbanList.count()
    }

    fun updateKanbanList(list: List<Kanban>) {
        kanbanList = list
        notifyDataSetChanged()
    }

    fun setListener(kanbanListener: OnKanbanListener) {
        listener = kanbanListener
    }

}