package com.example.notemvvm.data.relationship

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.notemvvm.data.Label
import com.example.notemvvm.data.Note

data class LabelsWithNotes(
    @Embedded val label: Label,
    @Relation(
        parentColumn = "labelId",
        entityColumn = "noteId",
        associateBy = Junction(NoteLabelCrossRef::class)
    )
    val notes:List<Note>
)