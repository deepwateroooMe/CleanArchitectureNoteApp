package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes.components

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case.NoteUseCases
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.NoteOrder
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.OrderType
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes.NotesEvent
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes.NotesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor (
    private val noteUseCases: NoteUseCases
) : ViewModel() {
    private val TAG = "test NotesViewModel"

    private val _state = mutableStateOf<NotesState>(NotesState())
    val state: State<NotesState> = _state
    private var recentlyDeletedNote: Note? = null
    private var getNotesJob: Job? = null

    init {
        Log.d(TAG, "init()")
        getNotes(NoteOrder.Date(OrderType.Descending))
    }
    
    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                // 先检查一下，用户所点击的排序，是否与当前排序相同（排序变量，和升降序）？如果是，就不用再做任何事了，不同才需要处理
                if (state.value.noteOrder::class == event.noteOrder::class
                    && state.value.noteOrder.orderType == event.noteOrder.orderType) return
                getNotes(event.noteOrder)
            }
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    // 这里是保存了最后一次删除的便签，那么同样，如果能最多恢复五次，可以保存一个list
                    recentlyDeletedNote = event.note
                }
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }

            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        Log.d(TAG, "getNotes()") 
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(noteOrder)
            .onEach { notes ->
                          _state.value = state.value.copy(
                              notes = notes,
                              noteOrder = noteOrder
                          )
            }
            .launchIn(viewModelScope)
    }
} 