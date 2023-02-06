package com.example.katify.view.listener

import com.example.katify.data.model.Kanban

/**
 * Implements [onClick] receiving [Kanban]
 */
interface OnKanbanListener {
    fun onClick(kanban: Kanban)
}