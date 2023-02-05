package com.example.katify.data.dao

import androidx.room.*
import com.example.katify.data.model.Kanban

@Dao
interface KanbanDao {
    @Query("SELECT * FROM kanban WHERE owner_id == :owner_id ORDER BY name")
    fun getKanbans(owner_id: String): List<Kanban>

    @Query("SELECT * FROM kanban WHERE id == :id")
    fun getKanban(id: Int): Kanban

    @Insert
    fun addKanban(kanban: Kanban):Long

    @Delete
    fun deleteKanban(kanban: Kanban)

    @Query("UPDATE kanban SET name = :name WHERE id =:id")
    fun updateKanban(name: String?, id: Int)
}