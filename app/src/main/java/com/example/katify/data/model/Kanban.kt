package com.example.katify.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="kanban")
data class Kanban(
    @ColumnInfo(name="name")
    var name: String = "New Kanban",

    @ColumnInfo(name="owner_id")
    var owner_id: String = "",
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id: Int = 0
}