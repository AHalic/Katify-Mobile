package com.example.katify.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.katify.data.dao.KanbanDao
import com.example.katify.data.dao.NoteDao
import com.example.katify.data.model.Kanban
import com.example.katify.data.model.Note

/**
 * The Room database for this app
 */
@Database(entities = [Kanban::class, Note::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun KanbanDao(): KanbanDao
    abstract fun NoteDao(): NoteDao

    companion object {

        // For Singleton instantiation
        private lateinit var INSTANCE: AppDatabase

        fun getInstance(context: Context): AppDatabase {

            if(!::INSTANCE.isInitialized) {

                synchronized(AppDatabase::class) {

                    INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, "katify.db")
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}