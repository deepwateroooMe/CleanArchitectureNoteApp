package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note

import androidx.compose.ui.focus.FocusState

sealed class AddEditNoteEvent {

    data class EnteredTitle(val value: String): AddEditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddEditNoteEvent()
    data class EnteredContent(val value: String):AddEditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState):AddEditNoteEvent()

    data class ChangeColor(val color: Int): AddEditNoteEvent() //
    object PickAColor: AddEditNoteEvent()  // 点击最的一个白色，去选择一个颜色

    object SaveNote: AddEditNoteEvent()
}