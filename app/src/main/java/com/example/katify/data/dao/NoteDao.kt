package com.example.katify.data.dao

import androidx.room.*
import com.example.katify.data.model.Kanban
import com.example.katify.data.model.KanbanNotes
import com.example.katify.data.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getNotes(): List<Note>

    /**
     * This query will tell Room to query both the [Kanban] and [Note] tables and handle
     * the object mapping.
     */
    @Transaction
    @Query("SELECT * FROM kanban WHERE id == :id AND id IN (SELECT DISTINCT(kanban_id) FROM note)")
    fun getKanbanNotes(id: Int): Flow<List<KanbanNotes>>

    @Insert
    fun saveNote(note: Note):Long

    @Delete
    fun deleteNote(note: Note)

    @Update
    fun updateNote(note: Note)

}