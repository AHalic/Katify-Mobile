package com.example.katify.data.model

import androidx.room.*

@Entity(tableName="note",
    foreignKeys = [
        ForeignKey(entity = Kanban::class, parentColumns = ["id"], childColumns = ["kanban_id"])
    ]
)
class Note {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var note_id: Int = 0

    @ColumnInfo(name="note_name")
    var note_name: String = ""

    @ColumnInfo(name="kanban_id")
    var kanban_id: Int = 0

    @ColumnInfo(name="content")
    var content : String = ""
}