package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Surface
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.NoteColorState
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.PickAColorScreen
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes.NotesScreen
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util.Screen
import com.plcoding.cleanarchitecturenoteapp.ui.CleanArchitectureNoteAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {
    private val TAG = "test MainActivity"

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate()")
        super.onCreate(savedInstanceState)
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
                        // // 主活动自动进入转入到 PickAColorScreen 中
                        // composable(route = Screen.PickAColorScreen.route) {
                        //     PickAColorScreen(navController = navController)
                        // }
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
                        // composable( // 这里加错了
                        //     route = Screen.AddEditNoteScreen.route +
                        //     "?notesId={noteId}&noteColor={noteColor}",
                        //     arguments = listOf(
                        //         navArgument(
                        //             name = "noteId"
                        //         ) {
                        //             type = NavType.IntType
                        //             defaultValue = -1
                        //         },
                        //         navArgument(
                        //             name = "noteColor"
                        //         ) {
                        //             type = NavType.IntType
                        //             defaultValue = -1
                        //         }, 
                        //     )
                        // ) {
                        //     val color = it.arguments?.getInt("noteColor") ?: -1
                        //     PickAColorScreen(
                        //         navController = navController,
                        //         noteColor = color
                        //     )
                        // }
                        // // 或者在这里写一个深层链接的 navDeepLink(),最好设置noteId不可为空
                        // val uri = "https://example.com"
                        // composable(
                            //     "pick_a_color?noteId={noteId}",
                            //     deepLinks = listOf(navDeepLink { uriPattern = "$uri/{id}" })
                            // ) { backStackEntry ->
                                //         Profile(navController, backStackEntry.arguments?.getString("id"))
                            // }
                    }
                }
            }
        }
    }
}
