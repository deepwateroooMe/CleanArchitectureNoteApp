package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.NoteColorState
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes.NotesScreen
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util.Screen
import com.plcoding.cleanarchitecturenoteapp.ui.CleanArchitectureNoteAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {
    private val TAG = "test MainActivity"

    @OptIn(ExperimentalPermissionsApi::class)
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate()")
        setContent {
            CleanArchitectureNoteAppTheme {
                Surface(
                    color = colors.background
                ) {
                    // 导航: 
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.NotesScreen.route
                    ) {
                        // 主活动自动进入转入到 NotesScreen 中
                        composable(route = Screen.NotesScreen.route) {
                            NotesScreen(navController = navController)
                        }
                        // 转到每个 便签 的 编写界面，用id和color字符串进行导航
                        composable(
                            route = Screen.AddEditNoteScreen.route +
                                    "?noteId={noteId}&noteColor={noteColor}",
                            arguments = listOf(
                                navArgument(
                                    name = "noteId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(
                                    name = "noteColor"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }, 
                            )
                        ) {
                            val color = it.arguments?.getInt("noteColor") ?: -1
                            AddEditNoteScreen(
                                navController = navController,
                                noteColorState = NoteColorState(color,false)
                            )
                        }
                    }
                }
            }
        }
    }
}
