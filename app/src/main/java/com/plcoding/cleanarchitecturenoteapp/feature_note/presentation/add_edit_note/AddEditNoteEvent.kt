package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note

import android.net.Uri
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color

sealed class AddEditNoteEvent {

    data class EnteredTitle(val value: String): AddEditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddEditNoteEvent()
    data class EnteredContent(val value: String): AddEditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState): AddEditNoteEvent()

    object ToggleNotePreview: AddEditNoteEvent()
    object ToggleColorSection: AddEditNoteEvent()
    object ToggleImageSection: AddEditNoteEvent()
    
    data class ChangeColor(val color: Color): AddEditNoteEvent() //
    data class ChangeCusColor(val color: Color): AddEditNoteEvent() //
    
    // 加载图片：先只考虑本地的
    data class LoadImageUrl(val url: String): AddEditNoteEvent()
    data class LoadImageUri(val uri: Uri): AddEditNoteEvent()
    object RemoveImage: AddEditNoteEvent()
    // data class RemoveImage(val noteImageState: NoteImageState): AddEditNoteEvent()

    data class ShowHideGalley(val showGalley: Boolean): AddEditNoteEvent()

    object SaveNote: AddEditNoteEvent()
}