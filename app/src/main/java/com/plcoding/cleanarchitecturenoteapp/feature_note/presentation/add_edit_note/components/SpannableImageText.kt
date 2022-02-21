package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build.VERSION.SDK_INT
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.size.OriginalSize
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util.GifImage
import java.net.URL

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun SpannableImageText(
    // str: String,
    imageID: Int,
    imageID2: Int,
    imageID3: Int
) {
    val annotatedString = buildAnnotatedString {
        append("爱表哥，爱生活~~")
        appendInlineContent(id = "imageId")
        append("好好学习，looking for Android developer jobs")
        appendInlineContent(id = "imageId2")
        append(" 我爱表哥，我爱学习~")
        appendInlineContent(id = "imageId3")
        append(" 好好学习，天天向上~")
    }

    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .componentRegistry {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder(context))
            } else {
                add(GifDecoder())
            }
        }
        .build()

    val inlineContentMap = mapOf(
        "imageId" to InlineTextContent(
            Placeholder(50.sp, 50.sp, PlaceholderVerticalAlign.TextCenter)
        ) {
            Image(
                painter = rememberImagePainter(
                    imageLoader = imageLoader,
                    data = imageID,
                    builder = {
                        size(OriginalSize)
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                // .padding(8.dp)
            )
        },
        "imageId2" to InlineTextContent(
            Placeholder(50.sp, 50.sp, PlaceholderVerticalAlign.TextCenter)
        ) {
            Image(
                painter = rememberImagePainter(
                    imageLoader = imageLoader,
                    data = imageID2,
                    builder = {
                        size(OriginalSize)
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                // .padding(8.dp)
            )
        },
        "imageId3" to InlineTextContent(
            Placeholder(50.sp, 50.sp, PlaceholderVerticalAlign.TextCenter)
        ) {
            Image(
                painter = rememberImagePainter(
                    imageLoader = imageLoader,
                    data = imageID3,
                    builder = {
                        size(OriginalSize)
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                // .padding(8.dp)
            )
        }
    )
    Text(annotatedString, inlineContent = inlineContentMap)
}
