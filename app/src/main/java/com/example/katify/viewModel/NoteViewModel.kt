package com.example.katify.viewModel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.katify.R
import com.example.katify.data.model.Note
import com.example.katify.data.room.AppDatabase
import com.example.katify.utils.Constants
import java.lang.Exception

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private var listMsg = MutableLiveData<Int>()
    private var addedMsg = MutableLiveData<Int>()
    private var noteList = MutableLiveData<List<Note>>()
    private var note = MutableLiveData<Note>()


    fun getListMsg(): LiveData<Int> {
        return listMsg
    }

    fun getNoteList(): LiveData<List<Note>> {
        return noteList
    }

    fun getIsAdded(): LiveData<Int> {
        return addedMsg
    }

    fun addNote(id:Int, name:String) {
        val p = Note(kanban_id=id, note_name = name)
        note.value = p

        val db = AppDatabase.getInstance(getApplication()).NoteDao()
        var resp = 0L
        return try {
            resp = db.saveNote(p)
            addedMsg.value = Constants.BD_MSGS.SUCCESS
        } catch (e: SQLiteConstraintException){
            addedMsg.value = Constants.BD_MSGS.CONSTRAINT
        }

    }

    fun getAllNotesOfKanban(id: Int) {
        val db = AppDatabase.getInstance(getApplication()).NoteDao()

        try {
            val resp = db.getKanbanNotes(id)
            if (resp == null) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                noteList.value = resp
            }
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }


}