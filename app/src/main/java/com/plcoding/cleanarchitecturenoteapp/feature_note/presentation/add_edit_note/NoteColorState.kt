package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note

import androidx.compose.ui.graphics.Color

data class NoteColorState(
    var color: Int = -1,
    val isColorSectionVisible: Boolean = false,

    // 去记住最后一次用户自定义的颜色: 这里的问题还没能解决彻底
    val cusColor: Int = -1 
)