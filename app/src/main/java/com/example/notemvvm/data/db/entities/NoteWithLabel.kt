package com.example.notemvvm.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class NoteWithLabel(
    val noteId:Int,
    var title: String,
    var body: String,
    var timestamp: Long,
    var pinned:Boolean = false,
    var labelId: Int?,
    var label: String?

)
