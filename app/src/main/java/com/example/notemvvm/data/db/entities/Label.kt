package com.example.notemvvm.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "label_table")
data class Label(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "labelId") val labelId: Int,
    @ColumnInfo(name = "label") var label: String
){
    constructor(label: String) : this(0,label){
        this.label = label
    }

    constructor() : this(0,"")

}