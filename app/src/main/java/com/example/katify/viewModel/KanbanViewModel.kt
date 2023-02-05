package com.example.katify.viewModel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.katify.data.model.Kanban
import com.example.katify.data.room.AppDatabase
import com.example.katify.utils.Constants
import java.lang.Exception

class KanbanViewModel(application: Application) : AndroidViewModel(application) {

    private var listMsg = MutableLiveData<Int>()
    private var addedMsg = MutableLiveData<Int>()
    private var updatedMsg = MutableLiveData<Int>()
    private var deleteMsg = MutableLiveData<Int>()
    private var kanbanList = MutableLiveData<List<Kanban>>()
    private var kanban = MutableLiveData<Kanban>()
    private var kanbanName = MutableLiveData<String>()


    fun getListMsg(): LiveData<Int> {
        return listMsg
    }

    fun getDeleteMsg(): LiveData<Int> {
        return deleteMsg
    }

    fun getKanbanList(): LiveData<List<Kanban>> {
        return kanbanList
    }

    fun getKanbanName(): LiveData<String> {
        return kanbanName
    }

    fun getIsAdded(): LiveData<Int> {
        return addedMsg
    }

    fun getIsUpdated(): LiveData<Int> {
        return updatedMsg
    }

    fun addKanban(id:String, name:String) {
        val p = Kanban(owner_id=id, name = name)
        kanban.value = p

        val db = AppDatabase.getInstance(getApplication()).KanbanDao()
        return try {
            db.addKanban(p)
            addedMsg.value = Constants.BD_MSGS.SUCCESS
        } catch (e: SQLiteConstraintException){
            addedMsg.value = Constants.BD_MSGS.CONSTRAINT
        }

    }

    fun updateKanban(id:Int, name:String?) {
        val db = AppDatabase.getInstance(getApplication()).KanbanDao()
        return try {
            db.updateKanban(name, id)
            updatedMsg.value = Constants.BD_MSGS.SUCCESS
        } catch (e: SQLiteConstraintException){
            updatedMsg.value = Constants.BD_MSGS.CONSTRAINT
        }

    }

    fun getAllKanbansOfOwner(id: String) {
        val db = AppDatabase.getInstance(getApplication()).KanbanDao()
        try {
            val resp = db.getKanbans(id)
            listMsg.value = Constants.BD_MSGS.SUCCESS
            kanbanList.value = resp
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    fun getKanban(id: Int) {
        val db = AppDatabase.getInstance(getApplication()).KanbanDao()
        try {
            val resp = db.getKanban(id)
            listMsg.value = Constants.BD_MSGS.SUCCESS
            kanbanName.value = resp.name
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    fun deleteKanbanAndNotes(kanban: Kanban) {
        val db = AppDatabase.getInstance(getApplication()).KanbanDao()
        val dbNotes = AppDatabase.getInstance(getApplication()).NoteDao()
        return try {
            dbNotes.deleteNotesFromKanban(kanban.id)
            db.deleteKanban(kanban)
            deleteMsg.value = Constants.BD_MSGS.SUCCESS
        } catch (e: SQLiteConstraintException){
            deleteMsg.value = Constants.BD_MSGS.CONSTRAINT
        }
    }
}