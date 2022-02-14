package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model

import androidx.compose.ui.graphics.Color.Companion.White
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.plcoding.cleanarchitecturenoteapp.ui.*

@Entity
data class Note(
    val title: String,
    val content: String,
    val timeStamp: Long,
    val color: Int,
    @PrimaryKey val id: Int? = null
) {
    // 常伴的五个圈，六个圈的取值
    companion object {
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink, White)
    }
}

class InvalidNoteException(message: String): Exception(message)








