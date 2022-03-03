package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.AddEditNoteEvent
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util.GallerySelect
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util.MyNotesPermissions
import kotlinx.coroutines.ExperimentalCoroutinesApi

// 这里还想要加入网络图片的地址框：url,所以就是三种可能性的判断，这里的逻辑比较混乱，已经打乱了原项目的架构
@SuppressLint("UnrememberedMutableState")
@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
@Composable
fun ImageMainContent(
    modifier: Modifier = Modifier,
     viewModel: AddEditNoteViewModel
) {
    val TAG = "test ImageMainContent"

    if (viewModel.imgUri.value != EMPTY_IMAGE_URI) {
        // Log.d(TAG, "viewModel.imgUri.value: " + viewModel.imgUri.value)
        var painter = rememberImagePainter(viewModel.imgUri.value)
        // Log.d(TAG, "(painter == null): " + (painter == null))
        // Log.d(TAG, "painter.value: " + painter)
        // 已经有了用户自=定义的图片了，显示图片，并显示删除重加选择
        Box(modifier = modifier) {
            Image(
                modifier = Modifier.fillMaxWidth(),
               painter = painter,
                contentDescription = "Captured image"
            )
            Button(
                modifier = Modifier.align(Alignment.BottomStart),
                onClick = {
                    Log.d(TAG, "remove img")
                    viewModel.imgUri.value = EMPTY_IMAGE_URI
                    viewModel.onEvent(AddEditNoteEvent.RemoveImage)
                }
            ) {
                Text("Remove image")
            }
        }
    } else {
//         var showGallery by remember { mutableStateOf(false) }
        if (viewModel.showGallery.value) {
            GallerySelect(
                modifier = modifier,
                onImageUri = { uri -> 
                    viewModel.showGallery.value = false
                    viewModel.imgUri.value = uri
                    viewModel.onEvent(AddEditNoteEvent.LoadImageUri(uri))
                }
            )
        } else {
            Box(modifier = modifier) {
                CameraCapture(
                    modifier = modifier,
                    onImageFile = { file ->
                                        Log.d(TAG, "onImageFile")
                                        viewModel.imgUri.value = file.toUri()
                                    Log.d(TAG, "file.toUri.toString(): " + file.toUri().toString())
                                    viewModel.onEvent(AddEditNoteEvent.LoadImageUri(file.toUri()))
                    }
                )
                Button(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(4.dp),
                    onClick = {
                        viewModel.showGallery.value = true
                    }
                ) {
                    Text("Select from Gallery")
                }
            }
        }
    }
}
