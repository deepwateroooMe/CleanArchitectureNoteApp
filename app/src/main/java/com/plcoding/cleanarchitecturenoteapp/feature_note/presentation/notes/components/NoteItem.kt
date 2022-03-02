package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes.components

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import coil.compose.rememberImagePainter
import com.plcoding.cleanarchitecturenoteapp.R
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.EMPTY_IMAGE_URI
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.RichEditText.GRicheditorViewComposable
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes.NotesEvent

@Composable // 每条便签在主界面中的显示界面
fun NoteItem (
    note: Note,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp,
    onDeleteClick: () -> Unit
) {
    val TAG = "test NoteItem"

    var painter = rememberImagePainter(
        Uri.parse(note.uri),
        builder = {
            placeholder(R.drawable.img)
        }
    )

    Box(
        modifier = modifier
    ) {
        // 画圆角矩形：并砍去折叠一个角
        Canvas(modifier = modifier.matchParentSize()) {
            val clipPath = Path().apply {
                lineTo(size.width - cutCornerSize.toPx(), 0f)
                lineTo(size.width, cutCornerSize.toPx())
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
            clipPath(clipPath) {
                drawRoundRect(
                    color = Color(note.color),
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
                drawRoundRect(
                    color = Color(
                        ColorUtils.blendARGB(note.color, 0x000000, 0.2f)
                    ),
                    topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
                    size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
        }
        // 这里的条件判断不太好
        // if (Uri.parse(note.uri) == EMPTY_IMAGE_URI) {
        // Row(
        //     modifier = Modifier
        //     // .fillMaxWidth()
        // ) {
        Column(
            modifier = (if (Uri.parse(note.uri) == EMPTY_IMAGE_URI) Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .padding(end = 32.dp)
                        else Modifier
                            .width(280.dp)
                            .padding(16.dp)
                            .padding(end = 32.dp)) as Modifier
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            // 想要使用这个东西，但需要修改
            GRicheditorViewComposable(
                modifier = Modifier,
                // .fillMaxWidth(),
                note.color,
                note.content
            )
            // Text(
                //     text = note.content,
                //     style = MaterialTheme.typography.body1,
                //     color = MaterialTheme.colors.onSurface,
                //     maxLines = 10,
                //     overflow = TextOverflow.Ellipsis
                // )
        }
        Spacer(modifier = Modifier.width(16.dp))
        // Column(
        //     modifier = Modifier
        //         .fillMaxWidth()
        // ) {
            // 如果有本地图片，这里加上图片
            Log.d(TAG, "note.uri: " + note.uri)
            Log.d(TAG, "(Uri.parse(note.uri) != EMPTY_IMAGE_URI): " + (Uri.parse(note.uri) != EMPTY_IMAGE_URI))
            if (Uri.parse(note.uri) != EMPTY_IMAGE_URI) {
                Log.d(TAG, "painter value: " + painter)
                Box(
                    modifier = Modifier
//                        .padding(16.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterEnd)
                ) {
                    // BUG #1: 如果是拍照，第一次可以正常加载；如果是从图库选择的图片，那么保存后退出，再进入，图片加载不成功
                    // 现在明白：因为使用了rememberImagePainter，所有的notes只会记住并显示最后个note的图片,明白原因，会修改
                    Image(
                        modifier = Modifier
//                        fillMaxWidth()
                        //                    .width(70.dp)
                             .size(200.dp) // 200
                        //                    .padding(7.dp)
                            .align(Alignment.CenterEnd),
                        // painter = painter,
                        painter = ImagePainter()
                        contentDescription = ""
                    )
                }
            }
            // 这里还是删除按钮
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete note"
                )
            }
        // }
    // }
    }
}