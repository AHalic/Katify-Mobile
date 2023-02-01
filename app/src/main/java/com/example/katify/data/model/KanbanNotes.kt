package com.example.katify.data.model

import androidx.room.*

data class KanbanNotes(
    @Embedded val kanban: Kanban,
    @Relation(
        parentColumn = "id",
        entityColumn = "kanban_id"
    )
    val notes: List<Note> = emptyList()
)