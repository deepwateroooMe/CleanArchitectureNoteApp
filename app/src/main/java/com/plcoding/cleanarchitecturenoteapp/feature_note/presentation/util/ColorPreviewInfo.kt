package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.me.colorpicker.HsvColor

@Composable
fun ColorPreviewInfo(currentColor: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // // a r g b 文本
        // Text(
        //     modifier = Modifier.padding(16.dp),
        //     text = "a: ${currentColor.alpha} \n" +
        //     "r: ${currentColor.red} \n" +
        //     "g: ${currentColor.green} \n" +
        //     "b: ${currentColor.blue}"
        // )
        // // 当前选定的颜色，圆形表示， 48.dp
        // Spacer(
        //     modifier = Modifier
        //         .background(
        //             currentColor, //
        //             shape = CircleShape
        //         )
        //         .size(50.dp)
        //         .align(Alignment.CenterHorizontally)
        // )
        // Spacer(Modifier.height(8.dp))
    }
}

// @Composable
// @OptIn(ExperimentalFoundationApi::class)
// fun ColorPaletteBar(
//     modifier: Modifier = Modifier,
//     colors: List<HsvColor>
// ) {
//     LazyVerticalGrid(
// //                    horizontalArrangement = Arrangement.spacedBy(4.dp),
// //                    verticalArrangement = Arrangement.spacedBy(4.dp),
//         cells = GridCells.Adaptive(48.dp),
//         modifier = modifier
//             .fillMaxWidth(),
//         contentPadding = PaddingValues(16.dp),
//         content = {
//             items(colors) {
//                 i ->
//                     Canvas(modifier = Modifier.size(48.dp)) {
//                         drawCircle(i.toColor())
//                     }
//             }
//         }
//     )
// }