package com.example.notemvvm.data.relationship

import androidx.room.Entity

@Entity(primaryKeys = ["noteId","labelId"])
data class NoteLabelCrossRef(
    val noteId: Int,
    val labelId: Int)