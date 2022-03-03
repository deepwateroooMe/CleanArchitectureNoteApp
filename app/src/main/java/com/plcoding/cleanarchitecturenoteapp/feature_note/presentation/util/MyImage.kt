package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun MyImage(
    uri: Uri,
    modifier: Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
//            .align(Alignment.CenterEnd)
    ) {
        Image(
            modifier = Modifier
            //                        fillMaxWidth()
            //                    .width(70.dp)
                .size(200.dp) // 200
            //                    .padding(7.dp)
                .align(Alignment.CenterEnd),
            painter = rememberImagePainter(uri),
            contentDescription = ""
        )
    }
}


//fun AsyncImage(
//    model = ImageRequest.Builder(LocalContext.current)
//        .data("https://example.com/image.jpg")
//        .crossfade(true)
//        .build(),
//    placeholder = painterResource(R.drawable.placeholder),
//    contentDescription = stringResource(R.string.description),
//    contentScale = ContentScale.Crop,
//    modifier = Modifier.clip(CircleShape)
//)
// fun MyImage (
// ) {
// }