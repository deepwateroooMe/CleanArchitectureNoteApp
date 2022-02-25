package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.RichEditText.AnnotatedString

@Composable
fun TransparentHintTextField (
    text: String,
    hint: String,
    modifier: Modifier,
    isHintVisible: Boolean = true,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    onFocusChange: (FocusState) -> Unit
) {
    // By default, composables aren’t selectable, which means by default users can't select and copy text from your app.
    // SelectionContainer {
    Box(
            modifier = modifier
        ) {
            BasicTextField(
                value = text,
                onValueChange = onValueChange,
                singleLine = singleLine,
                textStyle = textStyle,
                modifier = modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        onFocusChange(it)
                    }
            )
            if (isHintVisible) {
                Text(text = hint, style = textStyle, color = Color.DarkGray)
                // 有人说用ClickableText，其实并不推荐，因为ClickableText会直接consume掉所有的点击事件，对于点击事件的传递来说并不是一件好事，毕竟又不是整个Text都是可点击的，只是其中部分的内容可以点击 (这里说的是html中链接的点击，而不是文本的点击，查 关于Compose中Pointer事件的传递)
                // // 建立可点击可选择的文本编辑框
                // ClickableText(
                //     text = buildAnnotatedString {
                //         withStyle(style = SpanStyle(color = Color.DarkGray,
                //                                     fontWeight = FontWeight.Bold)
                //         ) {
                //             append(hint)
                //         }
                //     },
                //     onClick = {
                //         offset ->
                //             Log.d("ClickableText", "$offset -th character is clicked.")
                //     }
                // )
            }
        }
}
