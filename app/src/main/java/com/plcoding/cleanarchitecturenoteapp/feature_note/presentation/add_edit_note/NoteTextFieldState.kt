package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note

import org.w3c.dom.Text

data class NoteTextFieldState (
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)