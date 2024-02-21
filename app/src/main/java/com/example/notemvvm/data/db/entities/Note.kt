package com.example.notemvvm.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "noteId") val noteId:Int = 0,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "body") var body: String,
    @ColumnInfo(name = "timestamp") var timestamp: Long,
    @ColumnInfo(name = "pinned") var pinned:Boolean = false

)  {
    constructor(title: String,body: String,timestamp: Long,pinned:Boolean) : this(0, title, body, timestamp, pinned) {
        this.title = title
        this.body = body
        this.timestamp = timestamp
        this.pinned = pinned


    }

    constructor(pinned:Boolean) : this(0,"","",0,pinned) {
        this.pinned = pinned


    }

    constructor(noteId: Int = 0) : this(noteId,"","",0,false)
}
