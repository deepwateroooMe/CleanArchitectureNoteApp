package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note

import android.net.Uri
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.EMPTY_IMAGE_URI

data class NoteImageState(
    var uri: Uri = EMPTY_IMAGE_URI,
    var isImageSectionVisible: Boolean = false,
    var url: String = "",
)