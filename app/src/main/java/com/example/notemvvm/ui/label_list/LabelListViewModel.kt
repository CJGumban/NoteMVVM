package com.example.notemvvm.ui.label_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notemvvm.data.db.entities.Label
import com.example.notemvvm.data.db.entities.relationship.NoteLabelCrossRef
import com.example.notemvvm.data.db.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LabelListViewModel @Inject constructor(private val repository: NoteRepository): ViewModel() {
    private val _uiState = MutableStateFlow(LabelListUiState())
    val uiState:StateFlow<LabelListUiState> = _uiState.asStateFlow()
    var noteId = 0



    fun start(noteId: Int) {

        if (noteId == 0) {
          viewModelScope.launch{
            repository.getAllLabel().collect { labels ->
              _uiState.update {
                it.copy(
                  labelItems = labels,
                  addEditLabelMode = true
                )
              }
            }
          }
        } else {              Log.i(TAG, "hi how are you")


          viewModelScope.launch {
            repository.getAllNoteLabelCrossRef().combine(repository.getAllLabel()){
              noteLabelCrossRefs, labels ->
              Log.i(TAG, "${noteLabelCrossRefs}")
              Log.i(TAG, "${labels}")
              val noteLabelCrossRefItems: MutableList<NoteLabelCrossRefItem> = mutableListOf()
              labels.forEach { label ->
                if (noteLabelCrossRefs.contains(
                    NoteLabelCrossRef(
                      labelId = label.labelId,
                      noteId = noteId
                    )
                  )
                ) {
                  noteLabelCrossRefItems.add(NoteLabelCrossRefItem(label, true))
                } else {
                  noteLabelCrossRefItems.add(NoteLabelCrossRefItem(label, false))
                }
              }
              Log.i(TAG, "notelabelcrossrefItems ${noteLabelCrossRefItems}")
              _uiState.update {
                it.copy(
                  noteLabelsCrossRefItems = noteLabelCrossRefItems,
                  addEditLabelMode = false
                )
              }
              return@combine noteLabelCrossRefItems
            }.collect{value->
              Log.i(TAG, "hi how are you ${value}")
            }
          }
/*          viewModelScope.launch{
              repository.getAllNoteLabelCrossRef().collect(){
                noteLabelCrossRefs=it
                Log.i(TAG, "labelistfunction notelabelcrossref().collect ${noteLabelCrossRefs}")
              }
          }
          viewModelScope.launch{
              repository.getAllLabel().collect(){
                getAllLabels=it
                Log.i(TAG, "labellistfunction getAllLabels().collect ${getAllLabels}")


              }
          }*/


        }
    }

  fun insertLabel(label: Label) {
    viewModelScope.launch{ repository.upsertLabel(label) }
  }
  fun updateLabel(label: Label) {
    viewModelScope.launch{ repository.updateLabel(label) }
  }

  fun deleteLabel(label: Label){
    viewModelScope.launch{
      repository.deleteLabel(label)
      repository.deleteNoteLabelCrossRefByLabelId(label.labelId)
    }
  }

  fun insertNoteLabelCrossRef(noteLabelCrossRef: NoteLabelCrossRef){
    viewModelScope.launch {
      repository.insertNoteLabelCrossRef(noteLabelCrossRef)
    }
  }

  fun deleteNoteLabelCrossRef(noteLabelCrossRef: NoteLabelCrossRef){
    viewModelScope.launch {
      repository.deleteNoteLabelCrossRef(noteLabelCrossRef)
    }
  }

  companion object {
    const val TAG = "LabelListViewModel"
  }
}
