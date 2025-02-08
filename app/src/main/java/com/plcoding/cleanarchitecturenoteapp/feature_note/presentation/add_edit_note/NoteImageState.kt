package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note

import android.net.Uri

data class NoteImageState(
    var uri: Uri = EMPTY_IMAGE_URI,
    var isImageSectionVisible: Boolean = false,
    var url: String = "",
)