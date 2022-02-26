package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note

import android.graphics.drawable.Icon
import android.text.Spanned
import androidx.compose.material.icons.Icons
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.solver.widgets.Rectangle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.plcoding.cleanarchitecturenoteapp.R
import com.plcoding.cleanarchitecturenoteapp.R.drawable
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.*
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.RichEditText.GRicheditorViewComposable
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.html.HtmlText
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.html.annotatedStringResource
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.html.parseBold
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.html.parseHtml
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class,
    com.google.accompanist.permissions.ExperimentalPermissionsApi::class,
    androidx.compose.material.ExperimentalMaterialApi::class
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
    var notePreview = viewModel.notePreview.value

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
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save note"
                )
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
                            .border(
                                width = 3.dp,
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
                        viewModel.onEvent(AddEditNoteEvent.ToggleColorSection)
                    },
                ) {
                    Icon(
                        imageVector = Filled.Favorite,
                        contentDescription = "Favorite",
                        modifier = Modifier.size(150.dp),
                        tint = (if (colorCusState == -1) Color.Red else Color(colorCusState))
                    )
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(15.dp, CircleShape)
                            .background(
                                Color.Transparent,
                                shape = CircleShape 
                            )
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
                        viewModel.onEvent(AddEditNoteEvent.ToggleImageSection)
                        // 这时暂换一下：toggle Preview Mode 不需要设置这样的Mode
                        // Log.d(TAG, "onClick toggleNotePreview()")
                        // viewModel.onEvent(AddEditNoteEvent.ToggleNotePreview)
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
                PickAColorSection( 
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    noteColor = (if (colorCusState == -1) Color.Red else Color(colorCusState)),
                    onColorChange = {
                        viewModel.onEvent(AddEditNoteEvent.ChangeColor(it))
                        scope.launch {
                            noteBackgroundAnimatable.animateTo(
                                targetValue = it,
                                animationSpec = tween(
                                    durationMillis = 500
                                )
                            )
                            viewModel.onEvent(AddEditNoteEvent.ChangeCusColor(it))
                        }
                    }
                )
            }
            // // 设置编辑状态下的可选用便捷按钮,preview状态下隐藏: 这些都不需要了
            // AnimatedVisibility(
            //     visible = !notePreview,
            //     enter = fadeIn() + slideInVertically(),
            //     exit = fadeOut() + slideOutVertically()
            // ) {
            //     Row(
            //         modifier = Modifier
            //             .fillMaxWidth()
            //             .padding(2.dp), // 8.dp
            //         horizontalArrangement = Arrangement.SpaceBetween
            //     ) {
            //         Note.noteCommands.forEach {
            //             color ->
            //             Box(
            //                 modifier = Modifier
            //                     .weight(0.3f)
            //                     .height(32.dp)
            //                     .shadow(15.dp, RectangleShape)
            //                     .clip(RoundedCornerShape(10.dp)) // RectangleShape
            //                     .background(Color.White)
            //                     .clickable {
            //                     }
            //             )  {
            //                 Text(text = color,
            //                      color = MaterialTheme.colors.primary,
            //                      fontSize = 27.sp,
            //                      modifier = Modifier.align(Alignment.Center)
            //                 )
            //             }
            //         }
            //     }
            // }
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
            // if (!notePreview) {
            GRicheditorViewComposable(
                modifier = Modifier
                    .fillMaxWidth(),
                viewModel
            )
                // // 现在，我不想要这个东西了，想要改回传统自定义视图的多功能实现版块
                // TransparentHintTextField(
                //     text = contentState.text,
                //     hint = contentState.hint,
                //     onValueChange = {
                //         viewModel.onEvent((AddEditNoteEvent.EnteredContent(it)))
                //     },
                //     onFocusChange = {
                //         viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                //     },
                //     isHintVisible = contentState.isHintVisible,
                //     textStyle = MaterialTheme.typography.body1,
                //     // modifier = Modifier.fillMaxHeight() // 因为这里已经占据了整个屏幕，你是看不见后面再加的图片的
                //     modifier = Modifier.fillMaxWidth()
                // )

            Spacer(modifier = Modifier.height(16.dp))
            // 加载图片栏： 是 可见 可不见的，根据用户的点击状态来决定是否可
            AnimatedVisibility(
                visible = imageState.isImageSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                // SpannableImageText(R.drawable.love, R.drawable.study, R.drawable.faster)
                // GifImage(
                //     modifier = Modifier
                //         .fillMaxWidth()
                //         .padding(vertical = 16.dp),
                //     R.drawable.love
                // )
                ImageMainContent(
                    modifier = Modifier
                        .fillMaxWidth()
                    // .fillMaxHeight()
                        .padding(top = 8.dp) // adding some space to the label
                        .background(Color(colorState.color)),
                    viewModel
                )
            }
            // } else {
                // 这些是markdown的，可以用来参考的
                // val parser = org.commonmark.parser.Parser.builder().build()
                // val root = parser.parse(contentState.text) as Document
                // 显示render后的note的内容
//                 Surface(
//                     color = Color(noteColorState.color),
// //                    modifier = Modifier.doubleTapGestureFilter {
// //                        setRender(!render)
// //                }
//                 ) {
    
//     // 这里的数据源可以是html文本：就需要将Span转化为AnnotatedString
//                 Text(text = "Hello, I am <b> bold</b> text".parseBold())
//                 Spacer(modifier = Modifier.height(16.dp))
//                 Text("parseHtml: ")
//                 "Hello, I am <b> bold</b> text <br> Hello, I am <i> italic</i> text <br> Hello, I am <u> underline</u> text".parseHtml()
// //    Spanned.toAnnotatedString(R.string.tmp)
//                 Text("annotatedStringResource:  ")
//                 val tmp = annotatedStringResource(R.string.tmp, "UTF-8")
//                 Log.d(TAG, "tmp.toAnnotatedString()")
// //                Text(text = tmp.toAnnotatedString())
//                 Text(tmp)
//                 Spacer(modifier = Modifier.height(16.dp))
//                 Text("HtmlText: ")
//                 HtmlText("Hello, I am <b> bold</b> text <br> Hello, I am <i> italic</i> text <br> Hello, I am <u> underline</u> text")
                // IconButton(
                //     onClick = {
                //         Log.d(TAG, "onClick togglePreview")
                //         viewModel.onEvent(AddEditNoteEvent.ToggleNotePreview)
                //     },
                // ) {
                //     Box(
                //         modifier = Modifier
                //             .weight(0.6f)
                //             .height(32.dp)
                //             .shadow(15.dp, RectangleShape)
                //             .clip(RoundedCornerShape(10.dp)) // RectangleShape
                //             .background(Color.Black)
                //     )
                //     // Icon(
                //     //     painter = painterResource(id = R.drawable.preview),
                //     //     contentDescription = "Images",
                //     //     modifier = Modifier.weight(0.5f).height(32.dp)
                //     // )
                //     Text("<--Edit") // 现在不需要双模式切换，也就当然不再需要这个按钮了
                // }
        }
    }
}
