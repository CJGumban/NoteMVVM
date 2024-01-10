package com.example.notemvvm.data

import androidx.room.Embedded
import androidx.room.Relation

data class LabelWithNotes(
    @Embedded val label: Label,
    @Relation(
        parentColumn = "label_name",
        entityColumn = "label"
    )
    val notes: List<Note>
)
