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
    fun deleteKanban(kanban: Kanban):Int

    @Query("UPDATE kanban SET name = :name WHERE id =:id")
    fun updateKanban(name: String?, id: Int)


//    @Query("SELECT * FROM kanban WHERE growZoneNumber = :growZoneNumber ORDER BY name")
//    fun getPlantsWithGrowZoneNumber(growZoneNumber: Int): Flow<List<Plant>>
//
//    @Query("SELECT * FROM kanban WHERE id = :plantId")
//    fun getPlant(plantId: String): Flow<Plant>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAll(plants: List<Plant>)
}