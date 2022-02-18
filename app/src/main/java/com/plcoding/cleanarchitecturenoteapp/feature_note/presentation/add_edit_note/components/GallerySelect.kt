package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util

import android.Manifest
import android.Manifest.permission
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.EMPTY_IMAGE_URI

// const val EMPTY_IMAGE_URI = "https://nyc3.digitaloceanspaces.com/food2fork/food2fork-static/featured_images/500/featured_image.png"

@Composable
fun GallerySelect(
    modifier: Modifier = Modifier,
    onImageUri: (Uri) -> Unit = { }
) {
    val TAG = "test GallerySelect"

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            Log.d(TAG, "onResult bef onImageUri")
            Log.d(TAG, "uri.toString(): " + uri.toString())
            onImageUri((uri ?: EMPTY_IMAGE_URI) as Uri)
        }
    )

    @Composable
    fun LaunchGallery() {
        SideEffect {
            launcher.launch("image/*")
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MyNotesPermissions(
            permission = permission.ACCESS_MEDIA_LOCATION,
            rationale = "You want to read from photo gallery, so I'm going to have to ask for permission.",
            permissionNotAvailableContent = {
                Column(modifier) {
                    Text("O noes! No Photo Gallery!")
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Button(
                            modifier = Modifier.padding(4.dp),
                            onClick = {
                                context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                                          data = Uri.fromParts("package", context.packageName, null)
                                })
                            }
                        ) {
                            Text("Open Settings")
                        }
                        // If they don't want to grant permissions, this button will result in going back
                        Button(
                            modifier = Modifier.padding(4.dp),
                            onClick = {
                                onImageUri(EMPTY_IMAGE_URI as Uri)
                            }
                        ) {
                            Text("Use Camera")
                        }
                    }
                }
            },
        ) {
            LaunchGallery()
        }
    } else {
        LaunchGallery()
    }
}