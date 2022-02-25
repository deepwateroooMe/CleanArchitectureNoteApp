package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.plcoding.cleanarchitecturenoteapp.R
import kotlinx.coroutines.ExperimentalCoroutinesApi

const val DEFAULT_IMAGE = R.drawable.img

@Composable
fun ImageUtil(url: String, modifier: Modifier): Unit {
    Image(
        // imageResource(id = R.drawable.serios),
        // modifier = modifier,
        // contentScale = ContentScale.Crop
        painterResource(R.drawable.serios),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = modifier
    )    
    
//     val image = loadPicture(url, DEFAULT_IMAGE).value
//     image?.let { img ->
//                      Image(
//                          bitmap = img.asImageBitmap(),
//                          contentDescription = "img from file",
//                          modifier = modifier,
// //                         modifier = Modifier
// //                             .fillMaxWidth()
// //                             .height(IMAGE_HEIGHT.dp)
// //                         ,
//                          contentScale = ContentScale.Crop,
//                      )
//     }
}

@SuppressLint("UnrememberedMutableState")
@ExperimentalCoroutinesApi
@Composable
fun loadPicture(url: String, @DrawableRes defaultImage: Int): MutableState<Bitmap?> {

    val bitmapState: MutableState<Bitmap?> = mutableStateOf(null)

    // show default image while image loads
    // Glide.with(LocalContext.current)
    Glide.with(LocalContext.current)
        .asBitmap()
        .load(defaultImage)
        .into(object : CustomTarget<Bitmap>() {
                  override fun onLoadCleared(placeholder: Drawable?) { }
                  override fun onResourceReady(
                      resource: Bitmap,
                      transition: Transition<in Bitmap>?
                  ) {
                      bitmapState.value = resource
                  }
        })

    // get network image
    Glide.with(LocalContext.current)
        .asBitmap()
        .load(url)
        .into(object : CustomTarget<Bitmap>() {
                  override fun onLoadCleared(placeholder: Drawable?) { }
                  override fun onResourceReady(
                      resource: Bitmap,
                      transition: Transition<in Bitmap>?
                  ) {
                      bitmapState.value = resource
                  }
        })

    return bitmapState
}

@SuppressLint("UnrememberedMutableState")
@ExperimentalCoroutinesApi
@Composable
fun loadPicture(@DrawableRes drawable: Int): MutableState<Bitmap?> {

    val bitmapState: MutableState<Bitmap?> = mutableStateOf(null)

    Glide.with(LocalContext.current)
        .asBitmap()
        .load(drawable)
        .into(object : CustomTarget<Bitmap>() {
                  override fun onLoadCleared(placeholder: Drawable?) { }
                  override fun onResourceReady(
                      resource: Bitmap,
                      transition: Transition<in Bitmap>?
                  ) {
                      bitmapState.value = resource
                  }
        })

    return bitmapState
}
