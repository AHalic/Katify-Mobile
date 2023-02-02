package com.example.katify.viewModel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
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
    private var kanbanList = MutableLiveData<List<Kanban>>()
    private var kanban = MutableLiveData<Kanban>()


    fun getListMsg(): LiveData<Int> {
        return listMsg
    }

    fun getKanbanList(): LiveData<List<Kanban>> {
        return kanbanList
    }

    fun getIsAdded(): LiveData<Int> {
        return addedMsg
    }

    fun addKanban(id:String) {
        val p = Kanban(owner_id =id)
        kanban.value = p

        val db = AppDatabase.getInstance(getApplication()).KanbanDao()
        var resp = 0L
        return try {
            resp = db.addKanban(p)
            Log.w("RESP", "$resp")
            addedMsg.value = Constants.BD_MSGS.SUCCESS
        } catch (e: SQLiteConstraintException){
            addedMsg.value = Constants.BD_MSGS.CONSTRAINT
        }

    }

    fun getAllKanbansOfOwner(id: String) {
        val db = AppDatabase.getInstance(getApplication()).KanbanDao()
        try {
            val resp = db.getKanbans(id)
            if (resp == null) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                kanbanList.value = resp
            }
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }


}