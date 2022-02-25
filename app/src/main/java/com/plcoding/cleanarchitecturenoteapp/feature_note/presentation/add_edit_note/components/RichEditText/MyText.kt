package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.RichEditText

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextStyle

@Composable
fun Text(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle? = null,
    paragraphStyle: ParagraphStyle? = null,
    softWrap: Boolean = false, //DefaultSoftWrap,
//    overflow: TextOverflow = DefaultOverflow,
    maxLines: Int? = 50,//DefaultMaxLines,
    selectionColor: Color = Color.Black//DefaultSelectionColor
) {
    Text(
        text = AnnotatedString(text.text),
        modifier = modifier,
        style = style,
        paragraphStyle = paragraphStyle,
        softWrap = softWrap,
//        overflow = overflow,
        maxLines = maxLines,
        selectionColor = selectionColor
    )
}