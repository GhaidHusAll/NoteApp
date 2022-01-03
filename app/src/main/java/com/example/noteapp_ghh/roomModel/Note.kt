package com.example.noteapp_ghh.roomModel

import androidx.room.*

@Entity(tableName = "Notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    var pk: Long,
    var note: String,
    var id: String
    )