package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util

sealed class Screen (val route: String) {
    object NotesScreen: Screen("notes_Screen")
    object AddEditNoteScreen: Screen("add_edit_note_screen")

    object PickAColorScreen: Screen("pick_a_color")
}