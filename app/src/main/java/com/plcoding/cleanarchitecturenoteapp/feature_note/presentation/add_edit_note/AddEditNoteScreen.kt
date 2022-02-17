package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.plcoding.cleanarchitecturenoteapp.R
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.AddEditNoteViewModel
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.PickAColorSection
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.TransparentHintTextField
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util.DEFAULT_RECIPE_IMAGE
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util.loadPicture
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
    val state = viewModel.noteColor.value

    val scaffoldState = rememberScaffoldState()

    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(if (noteColorState.color != -1) noteColorState.color else viewModel.noteColor.value.color)
        )
    }

    val scope = rememberCoroutineScope()

    val url = "https://nyc3.digitaloceanspaces.com/food2fork/food2fork-static/featured_images/500/featured_image.png"
//    val imageState = ImageUtil(
//        context = LocalContext.current,
//        url = url,
//    )

    // LaunchEffect允许我们在Composable中使用协程
    // 让Composable支持协程的重要意义是，可以让一些简单的业务逻辑直接以Composable的形式封装并实现复用，而无需额外借助ViewModel
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
                    // navController.navigate(AddEditNoteScreen.route) // 拿来参考的,没用上
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
                        // 这里把边框去掉了，只是埋掉了一个bug，因为我新的兴趣在，暂时不处理这个bug,改天回来再改
                            // .border(
                            //     width = 3.dp,
                            //     // 这里是画圆圈边圈的颜色:感觉更新得不对，总是上一次的颜色在画圈
                            //     color = if (viewModel.noteColor.value.color == colorInt) {
                            //         Color.Black
                            //     } else {
                            //         Color.Transparent
                            //     },
                            //     shape = CircleShape
                            // )
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
                IconButton( // 用户自定义颜色: 心形需要画得再大一点儿
                            onClick = {
                                viewModel.onEvent(AddEditNoteEvent.ToggleColorSection)
                            },
                ) { 
                    // Box (
                    //     modifier = Modifier
                    //         .size(50.dp)
                    //         .shadow(15.dp, CircleShape)
                    //         .background(
                    //             // 这里把它设定为了state值更新前的value，即上一次选定的颜色，所以会受其它按钮的影响，可以用一个值把它记住，就不会悥记了
                    //             // Color(if (Note.cusColor != -1) Note.cusColor else viewModel.noteColor.value.color), // 这里仍然不对
                    //             Color(state.color), 
                    //             shape = CircleShape // 想把这里改成心形
                    //         )
                    //     // // 如果我定义了圆圈的黑圈描边，那么会仍然需要控制变量、当点击了其它背景颜色时来取消现自定义黑圈边，不如暂且简单不管它
                    //     //     .border(
                    //     //         width = 3.dp,
                    //     //         // 这里是画圆圈边圈的颜色
                    //     //         color = if (Color(state.color) != Color.Black) {
                    //     //             Color.Black
                    //     //         } else {
                    //     //             Color.Transparent
                    //     //         },
                    //     //         shape = CircleShape
                    //     //     )
                    // )
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favorite",
                        modifier = Modifier.size(150.dp),
                        tint = (if (state.color == -1) Color.Red else Color(state.color))
                    )
                }
                IconButton( // 再加一个imagebutton
                            onClick = {
                                Log.d(TAG, "imageButon() onClick()")
//                               viewModel.onEvent(AddEditNoteEvent.LoadImage(url))
                            },
                ) { 
                    Icon(
                        imageVector = Icons.Filled.Image,
                        contentDescription = "Images",
                        modifier = Modifier.size(150.dp)
                    )
                }
                // Card(modifier = Modifier
                //          .padding(4.dp)
                //          .clickable { }) {
                //     //如果图片加载成功
                //     imageState.value?.let {
                //         Image(
                //             bitmap = it.asImageBitmap(),
                //             contentDescription = "",
                //             modifier = Modifier
                //                 .padding(4.dp)
                //                 .fillMaxWidth()
                //         )
                //     }
                // }
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
            Image( // 本地png图片，ok
                painter = painterResource(R.drawable.img),
                contentDescription = "icon"
            )
            Spacer(modifier = Modifier.height(16.dp))
            val image = loadPicture(url = url, defaultImage = DEFAULT_RECIPE_IMAGE).value
            image?.let { img ->
                             Image(
                                 bitmap = img.asImageBitmap(),
                                 contentDescription = "loaded internet imgaes",
                                 modifier = Modifier
                                     .fillMaxWidth()
                                     .height(225.dp),
                                 contentScale = ContentScale.Crop,
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
            Spacer(modifier = Modifier.height(16.dp))


            // Spacer(modifier = Modifier.height(16.dp))
                            //            LazyColumn {
                                // LoadPicture(url = url)
                                //     val urls = arrayListOf<String>()
            //     for (i in 500..501){urls.add("https://nyc3.digitaloceanspaces.com/food2fork/food2fork-static/featured_images/$i/featured_image.png")}
            //     itemsIndexed(urls){ _ , url -> LoadPicture(url = url)}
//            }
            // Image(bitmap = ImageBitmap.imageResource(id = R.mipmap.ic_launcher),
            //       contentDescription = "icon"
            // )            
        }
    }
}