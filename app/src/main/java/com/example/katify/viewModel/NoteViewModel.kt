package com.example.katify.viewModel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.katify.data.model.Kanban
import com.example.katify.data.model.Note
import com.example.katify.data.room.AppDatabase
import com.example.katify.utils.Constants
import java.lang.Exception

/**
 * This class interacts with the [Note] entity
 *
 * Inherits [AndroidViewModel]
 *
 * @property application
 */
class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private var listMsg = MutableLiveData<Int>()
    private var addedMsg = MutableLiveData<Int>()
    private var updateNameMsg = MutableLiveData<Int>()
    private var updateDescMsg = MutableLiveData<Int>()
    private var deleteMsg = MutableLiveData<Int>()
    private var noteList = MutableLiveData<List<Note>>()
    private var note = MutableLiveData<Note>()


    /**
     * Gets response of the db interaction at [getAllNotesOfKanban]
     *
     * @return message of success or failure
     */
    fun getListMsg(): LiveData<Int> {
        return listMsg
    }

    /**
     * Gets response of the db interaction at [deleteNote]
     *
     * @return message of success or failure
     */
    fun getDeleteMsg(): LiveData<Int> {
        return deleteMsg
    }

    /**
     * Gets response to the db interaction at [getAllNotesOfKanban]
     *
     * @return list of [Note]
     */
    fun getNoteList(): LiveData<List<Note>> {
        return noteList
    }

    /**
     * Gets response of the db interaction at [addNote]
     *
     * @return message of success or failure
     */
    fun getIsAdded(): LiveData<Int> {
        return addedMsg
    }

    /**
     * Gets response of the db interaction at [updateNoteName]
     *
     * @return message of success or failure
     */
    fun getIsNameUpdated(): LiveData<Int> {
        return updateNameMsg
    }

    /**
     * Gets response of the db interaction at [updateNoteDesc]
     *
     * @return message of success or failure
     */
    fun getIsDescUpdated(): LiveData<Int> {
        return updateDescMsg
    }

    /**
     * Adds [Note] with [name] of kanban with [id] to the db
     *
     */
    fun addNote(id:Int, name:String) {
        val p = Note(kanban_id=id, note_name = name)
        note.value = p

        val db = AppDatabase.getInstance(getApplication()).NoteDao()
        return try {
            db.saveNote(p)
            addedMsg.value = Constants.BD_MSGS.SUCCESS
        } catch (e: SQLiteConstraintException){
            addedMsg.value = Constants.BD_MSGS.CONSTRAINT
        }

    }

    /**
     * Gets list of [Note] of kanban with [id] from the db
     *
     */
    fun getAllNotesOfKanban(id: Int) {
        val db = AppDatabase.getInstance(getApplication()).NoteDao()

        try {
            val resp = db.getKanbanNotes(id)
            listMsg.value = Constants.BD_MSGS.SUCCESS
            noteList.value = resp
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    /**
     * Updates [Note.note_name] of [id] to [name] in the db
     *
     */
    fun updateNoteName(id:Int, name:String?) {
        val db = AppDatabase.getInstance(getApplication()).NoteDao()
        return try {
            db.updateNoteName(name, id)
            updateNameMsg.value = Constants.BD_MSGS.SUCCESS
        } catch (e: SQLiteConstraintException){
            updateNameMsg.value = Constants.BD_MSGS.CONSTRAINT
        }
    }

    /**
     * Updates [Note.content] of [id] to [name] in the db
     *
     */
    fun updateNoteDesc(id:Int, desc:String?) {
        val db = AppDatabase.getInstance(getApplication()).NoteDao()
        return try {
            db.updateNoteDesc(desc, id)
            updateDescMsg.value = Constants.BD_MSGS.SUCCESS
        } catch (e: SQLiteConstraintException){
            updateDescMsg.value = Constants.BD_MSGS.CONSTRAINT
        }
    }

    /**
     * Deletes [note]  from the db
     *
     */
    fun deleteNote(note: Note) {
        val db = AppDatabase.getInstance(getApplication()).NoteDao()
        return try {
            db.deleteNote(note)
            deleteMsg.value = Constants.BD_MSGS.SUCCESS
        } catch (e: SQLiteConstraintException){
            deleteMsg.value = Constants.BD_MSGS.CONSTRAINT
        }
    }
}