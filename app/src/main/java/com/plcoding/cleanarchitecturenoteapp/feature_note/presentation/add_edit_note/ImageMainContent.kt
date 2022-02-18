package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.AddEditNoteEvent
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util.GallerySelect
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util.MyNotesPermissions
import kotlinx.coroutines.ExperimentalCoroutinesApi

// val EMPTY_IMAGE_URI: Uri = Uri.parse("file://dev/null")

// 这里还想要加入网络图片的地址框：url,所以就是三种可能性的判断，这里的逻辑比较混乱，已经打乱了原项目的架构，感觉
// 现在保存图片，有点儿不知道从哪里开始
@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
@Composable
fun ImageMainContent(
    modifier: Modifier = Modifier,
     // imageUri: MutableState<Uri>,
     // showGallerySelect: MutableState<Boolean>,
    // imageUri: MutableState<Uri> = mutableStateOf(EMPTY_IMAGE_URI),
    // showGallerySelect: MutableState<Boolean>
     viewModel: AddEditNoteViewModel
) {
    // private var _imgUri = mutableStateOf(EMPTY_IMAGE_URI)
    // val imgUri: MutableState<Uri> = _imgUri

    // private val _showGallery = mutableStateOf<Boolean>(false)
    // val showGallery: MutableState<Boolean> = _showGallery

    val TAG = "test ImageMainContent"
    Log.d(TAG, "(imageUri != EMPTY_IMAGE_URI): " + (viewModel.imgUri.value != EMPTY_IMAGE_URI))
    Log.d(TAG, "showGallery.value: " + viewModel.showGallery.value)
    Log.d(TAG, "imageUri.toString(): " + viewModel.imgUri.toString())

    //     var imageUri by remember { mutableStateOf(EMPTY_IMAGE_URI) }
    if (viewModel.imgUri.value != EMPTY_IMAGE_URI) {
        Box(modifier = modifier) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberImagePainter(viewModel.imgUri.value),
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
                                   Log.d(TAG, "onImageUri img selected")
                               Log.d(TAG, "uri.toString(): " + uri.toString())
                    viewModel.showGallery.value = false
                    // viewModel.onEvent(AddEditNoteEvent.ToggleGallerySection)
                    viewModel.imgUri.value = uri
                    viewModel.onEvent(AddEditNoteEvent.LoadImageUri(uri))
                }
            )
        } else {
            Box(modifier = modifier) {
                CameraCapture(
                    modifier = modifier,
                    onImageFile = { file ->
                                        viewModel.imgUri.value = file.toUri()
                                    Log.d(TAG, "imgaeUri.value.toString(): " + viewModel.imgUri.value.toString())
                                    viewModel.onEvent(AddEditNoteEvent.LoadImageUri(file.toUri()))
                    }
                )
                Button(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(4.dp),
                    onClick = {
                        viewModel.showGallery.value = true
                        // viewModel.onEvent(AddEditNoteEvent.ToggleGallerySection)
                        // viewModel.onEvent(AddEditNoteEvent.RemoveImage())
                    }
                ) {
                    Text("Select from Gallery")
                }
            }
        }
    }
}
