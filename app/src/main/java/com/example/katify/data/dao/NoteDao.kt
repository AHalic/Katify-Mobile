package com.example.katify.data.dao

import androidx.room.*
import com.example.katify.data.model.Kanban
import com.example.katify.data.model.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getNotes(): List<Note>

    /**
     * This query will tell Room to query both the [Kanban] and [Note] tables and handle
     * the object mapping.
     */
    @Transaction
    @Query("SELECT * FROM note WHERE kanban_id == :id AND kanban_id IN (SELECT DISTINCT(id) FROM kanban)")
    fun getKanbanNotes(id: Int): List<Note>

    @Insert
    fun saveNote(note: Note):Long

    @Delete
    fun deleteNote(note: Note)

    @Query("DELETE FROM note WHERE kanban_id == :id")
    fun deleteNotesFromKanban(id: Int)

    @Query("UPDATE note SET note_name = :name WHERE id =:id")
    fun updateNoteName(name: String?, id: Int)

    @Query("UPDATE note SET content = :desc WHERE id =:id")
    fun updateNoteDesc(desc: String?, id: Int)

}