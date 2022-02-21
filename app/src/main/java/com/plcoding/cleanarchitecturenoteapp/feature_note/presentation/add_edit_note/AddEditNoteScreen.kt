package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note

import android.net.Uri
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.plcoding.cleanarchitecturenoteapp.R
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.*
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util.DEFAULT_RECIPE_IMAGE
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util.GallerySelect
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util.GifImage
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util.loadPicture
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class,
    com.google.accompanist.permissions.ExperimentalPermissionsApi::class
)
@Composable // 每条便签一编写界面
fun AddEditNoteScreen (
    navController: NavController,
    noteColorState: NoteColorState,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val TAG = "test AddEditNoteScreen"

    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    val colorState = viewModel.noteColor.value
    val colorCusState = viewModel.noteCusColor.value
    val imageState = viewModel.noteImage.value

    val imageUri = viewModel.imgUri
    var showGallery = viewModel.showGallery

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(if (noteColorState.color != -1) noteColorState.color else colorState.color)
        )
    }

    // LaunchEffect允许我们在Composable中使用协程
    // 让Composable支持协程的重要意义是，可以让一些简单的业务逻辑直接以Composable的形式封装并实现复用，而无需额外借助ViewModel
    // key1 = true so that execute only once
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
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
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditNoteEvent.SaveNote)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save note")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Note.noteColors.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(width = 3.dp,
                                color = if (colorState.color == colorInt) {
                                    Color.Black
                                } else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    viewModel.onEvent(AddEditNoteEvent.ChangeColor(color))
                                    noteBackgroundAnimatable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                }
                            }
                    )
                }
                IconButton(
                    onClick = {
                        // 暂且把这个替换一下
                        viewModel.onEvent(AddEditNoteEvent.ToggleColorSection)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favorite",
                        modifier = Modifier.size(150.dp),
                        tint = (if (colorCusState == -1) Color.Red else Color(colorCusState))
                    )
                    // 想要这个box在用户自定义颜色的时候周边黑圆圈高亮，所以设置透明背景（而不是填用户自定义的颜色，填了宝贝爱心形状就被掩盖掉了。。。）
                    Box (modifier = Modifier
                            .size(50.dp)
                            .shadow(15.dp, CircleShape)
                            .background(
                                // 这里把它设定为了colorState值更新前的value，即上一次选定的颜色，所以会受其它按钮的影响，可以用一个值把它记住，就不会悥记了
                                // Color(colorCusState),
                                Color.Transparent,
                                shape = CircleShape // 想把这里改成心形
                            )
                        // 如果我定义了圆圈的黑圈描边，那么需要根据背景的颜色来判断：当前背景是否是自定义的背景，是则描黑边，否则就透明不描
                            .border(
                                width = 3.dp,
                                color = if (Color(colorCusState) != noteBackgroundAnimatable.value) {
                                    Color.Transparent
                                } else {
                                    Color.Black
                                },
                                shape = CircleShape
                            )
                    )
                }
                IconButton(
                    onClick = {
                        Log.d(TAG, "onClick toggleImageSection")
                        viewModel.onEvent(AddEditNoteEvent.ToggleImageSection)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Image,
                        contentDescription = "Images",
                        modifier = Modifier.size(150.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // 选择自定义颜色栏 是 可见 可不见的，根据用户的点击状态来决定是否可见
            AnimatedVisibility(
                visible = colorState.isColorSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                GifImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    R.drawable.love
                )

                // PickAColorSection( // 暂且暂换一下
                //     modifier = Modifier
                //         .fillMaxWidth()
                //         .padding(vertical = 16.dp),
                //     noteColor = (if (colorCusState == -1) Color.Red else Color(colorCusState)),
                //     onColorChange = {
                //         viewModel.onEvent(AddEditNoteEvent.ChangeColor(it))
                //         scope.launch {
                //             noteBackgroundAnimatable.animateTo(
                //                 targetValue = it,
                //                 animationSpec = tween(
                //                     durationMillis = 500
                //                 )
                //             )
                //             viewModel.onEvent(AddEditNoteEvent.ChangeCusColor(it))
                //         }
                //     }
                // )
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
                // modifier = Modifier.fillMaxHeight() // 因为这里已经占据了整个屏幕，你是看不见后面再加的图片的
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // 加载图片栏： 是 可见 可不见的，根据用户的点击状态来决定是否可
            AnimatedVisibility(
                visible = imageState.isImageSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                ImageMainContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(top = 8.dp) // adding some space to the label
                        .background(Color(colorState.color)),
                    viewModel
                )
            }
        }
    }
}