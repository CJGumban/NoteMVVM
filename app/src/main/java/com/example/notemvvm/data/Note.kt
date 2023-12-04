package com.example.notemvvm.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id:Int,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "body") var body: String,
    @ColumnInfo(name = "timestamp") var timestamp: Long,
    @ColumnInfo(name = "label") var label: String?,
    @ColumnInfo(name = "pinned") var pinned:Boolean
){
    constructor(title: String,body: String,timestamp: Long,label: String?,pinned:Boolean) : this(0, title, body, timestamp, label, pinned) {
        this.title = title
        this.body = body
        this.timestamp = timestamp
        this.label = label
        this.pinned = pinned


    }
}
