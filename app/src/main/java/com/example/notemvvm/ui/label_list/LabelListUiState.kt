package com.example.notemvvm.ui.label_list

import com.example.notemvvm.data.db.entities.Label

data class LabelListUiState(
    val addEditLabelMode: Boolean = true,
    val labelItems: List<Label> = listOf(),
    val noteLabelsCrossRefItems: List<NoteLabelCrossRefItem> = listOf()
)
data class NoteLabelCrossRefItem(
    val label: Label = Label(),
    val isChecked: Boolean = false
)