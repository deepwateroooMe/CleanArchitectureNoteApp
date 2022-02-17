package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.InvalidNoteException
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case.NoteUseCases
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.AddEditNoteEvent
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.NoteColorState
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.NoteTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val TAG = "test AddEditNoteViewModel"

    private val _noteTitle = mutableStateOf(NoteTextFieldState(
        hint = "Enter title..."
    ))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(
        hint = "Enter some content..."
    ))
    val noteContent: State<NoteTextFieldState> = _noteContent

    // private val _noteColor = mutableStateOf<Int>(Note.noteColors.random().toArgb())
    // val noteColor: State<Int> = _noteColor
    private val _noteColor = mutableStateOf<NoteColorState>(NoteColorState(Note.noteColors.random().toArgb(), false))
    val noteColor: State<NoteColorState> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

//     private val _noteImage = mutableStateOf(ImageState())
//     val noteImage: State<ImageState> = _noteImage

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        // _noteColor.value = note.color
                        _noteColor.value = noteColor.value.copy(
                            color = noteColor.value.color,
                            isColorSectionVisible = noteColor.value.isColorSectionVisible
                        )
                    }
                }
            }
        }
    }
    fun onEvent(event: AddEditNoteEvent) {
        when(event) {
            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timeStamp = System.currentTimeMillis(),
                                color = noteColor.value.color,
                                null,
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Coundn't save note"
                            )
                        )
                    }
                }
            }

            // 选择一个favoriate颜色: 这里要理解一下event的事件传递方向
            is AddEditNoteEvent.ToggleColorSection -> {
                _noteColor.value = noteColor.value.copy(
                    isColorSectionVisible = !noteColor.value.isColorSectionVisible
                )
            }
            is AddEditNoteEvent.ChangeColor -> {
                 _noteColor.value.color = event.color.toArgb()
//                _noteColor.value = event.noteColorState.value.copy(
//                    color = noteColor.value.color,
//                    isColorSectionVisible = noteColor.value.isColorSectionVisible
//                )
            }
//            // hard coded
//            is AddEditNoteEvent.LoadImage -> {
//                _noteImage.value = noteImage.value.copy(
//                    text = event.value
//                )
//                // val url = "https://nyc3.digitaloceanspaces.com/food2fork/food2fork-static/featured_images/500/featured_image.png"
//            }
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }     
            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused
                    && _noteTitle.value.text.isBlank()
                )
            }     
            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = _noteContent.value.copy(
                    text = event.value
                )
            }     
            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = _noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused
                            && _noteContent.value.text.isBlank()
                )
            }     
        }
    }
    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveNote: UiEvent()

        object PickAColor: UiEvent()
    }
}