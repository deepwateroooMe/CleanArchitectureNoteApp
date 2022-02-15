package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.AddEditNoteViewModel
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.PickAColorSection
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.TransparentHintTextField
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes.NotesEvent
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util.Screen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable // 每条便签一编写界面
fun AddEditNoteScreen (
    navController: NavController,
    noteColorState: NoteColorState,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val TAG = "test AddEditNoteScreen"

    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value

    val scaffoldState = rememberScaffoldState()

    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(if (noteColorState.color != -1) noteColorState.color else viewModel.noteColor.value.color)
        )
    }
    val scope = rememberCoroutineScope()

    val state = viewModel.noteColor.value

    // key1 = true so that execute only once
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditNoteEvent.SaveNote)
                    // navController.navigate(AddEditNoteScreen.route) // 拿来参考的
                    // PickAColorScreen(navController).route
                    // PickAColorScreen(navController) // 
                    // navController.navigate(PickAColorScreen(navController)) 
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save note")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(16.dp)
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Note.noteColors.forEach {
                    color -> 
                        val colorInt = color.toArgb()
                    Box (
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(15.dp, CircleShape)
                            .background(color)
                            .border(
                                width = 3.dp,
                                // 这里是画圆圈边圈的颜色:感觉更新得不对，总是上一次的颜色在画圈
                                color = if (viewModel.noteColor.value.color == colorInt) {
                                    Color.Black
                                } else {
                                    Color.Transparent
                                },
                                shape = CircleShape
                            )
                            // 这里是需要分情况进行了，点到最后一个白色，会多出一个步骤：选择颜色
                            .clickable {
                                scope.launch {
                                    noteBackgroundAnimatable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                    viewModel.onEvent(AddEditNoteEvent.ChangeColor(color))
                                }
                            }
                    )
                }
                // 用户自定义颜色: 心形需要画得再大一点儿
                IconButton( 
                            onClick = {
                                // 这里无法真正改变背景颜色
                                viewModel.onEvent(AddEditNoteEvent.ToggleColorSection)
                                // // 只有当选定了一种自定义颜色时，才更换背景界面
                                // if (!state.isColorSectionVisible) {
                                    // Log.d(TAG, "IconButton() isColorSectionVisible = false 2nd") 
                                    // scope.launch {
                                    //     noteBackgroundAnimatable.animateTo(
                                    //         targetValue = Color(state.color), 
                                    //         animationSpec = tween(
                                    //             durationMillis = 500
                                    //         )
                                    //     )
                                    //     viewModel.onEvent(AddEditNoteEvent.ChangeColor(Color(state.color)))
                                    // }
                                    // }
                            },
                ) { // 这里理想的情况是想要把它画成心形的，表达favorite之意
                    Box (
                        modifier = Modifier
                            .size(50.dp)
                            .background(
                                Color(state.color),
                                shape = CircleShape // 想把这里改成心形
                            )
                        // // 如果我定义了圆圈的黑圈描边，那么会仍然需要控制变量、当点击了其它背景颜色时来取消现自定义黑圈边，不如暂且简单不管它
                        //     .border(
                        //         width = 3.dp,
                        //         // 这里是画圆圈边圈的颜色
                        //         color = if (Color(state.color) != Color.Black) {
                        //             Color.Black
                        //         } else {
                        //             Color.Transparent
                        //         },
                        //         shape = CircleShape
                        //     )
                    )
                    // Spacer(Modifier.height(16.dp))
                    // Icon(
                    //     imageVector = Icons.Filled.Favorite,
                    //     contentDescription = "Favorite"
                    // )
                    //                    // 最好是把周边描一圈黑边
//                    Box(
//                        modifier = Modifier.size(100.dp).clip(CutCornerShape(10.dp)).background(Color.Transparent)
//                    )
                    // Text("Love")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // 选择自定义颜色栏 是 可见 可不见的，根据用户的点击状态来决定是否可见
            AnimatedVisibility(
                visible = state.isColorSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                PickAColorSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    noteColor = Color(state.color),
                    onColorChange = {
                        Log.d(TAG, "onColorChange()") 
                        Log.d(TAG, "it: " + it) // 这里的值是对的， 下面一行的值不对
                        viewModel.onEvent(AddEditNoteEvent.ChangeColor(it))
                        scope.launch {
                            noteBackgroundAnimatable.animateTo(
                                targetValue = it, 
                                animationSpec = tween(
                                    durationMillis = 500
                                )
                            )
                            // viewModel.onEvent(AddEditNoteEvent.ChangeColor(Color(state.color)))
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                                viewModel.onEvent((AddEditNoteEvent.EnteredTitle(it)))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.h5,
                modifier = Modifier 
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    viewModel.onEvent((AddEditNoteEvent.EnteredContent(it)))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                },
                isHintVisible = contentState.isHintVisible,
                textStyle = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}