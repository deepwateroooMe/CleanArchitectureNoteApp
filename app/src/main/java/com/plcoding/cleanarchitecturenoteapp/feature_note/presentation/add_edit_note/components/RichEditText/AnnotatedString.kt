package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.RichEditText

import android.util.Log
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight

data class AnnotatedString(
    val text: String,
    val textStyles: List<TextStyle> = listOf(),
    val paragraphStyles: List<ParagraphStyle> = listOf()
)

@Composable
fun AnnotatedClickableText() {
    val annotatedText = buildAnnotatedString {
        append("Click ")

        // We attach this *URL* annotation to the following content
        // until `pop()` is called
        pushStringAnnotation(tag = "URL",
                             annotation = "https://developer.android.com")
        withStyle(style = SpanStyle(color = Color.Blue,
                                    fontWeight = FontWeight.Bold)
        ) {
            append("here")
        }
        pop()
    }
    ClickableText(
        text = annotatedText,
        onClick = { offset ->
                        // We check if there is an *URL* annotation attached to the text
                    // at the clicked position
                    annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                        .firstOrNull()?.let { annotation ->
                                                  // If yes, we log its value
                                              Log.d("Clicked URL", annotation.item)
                        }
        }
    )
}


// constructor(text: String)
// constructor(text: AnnotatedString)
// fun append(text: String) { ... }
// fun append(char: Char) { ... }
// fun append(text: AnnotatedString) { ... }
// fun addStyle(style: TextStyle, start: Int, end: Int) { ... }
// fun addStyle(style: ParagraphStyle, start: Int, end: Int) { ... }

// val style = TextStyle(color = Color.Red)
// val annotatedString = AnnotatedString.Builder("Hello")
// annotatedString.pushStyle(style)
// annotatedString.append(", some more text!")
// annotatedString.popStyle()
// annotatedString.append(" And some more.")
// Text(text = annotatedString.toAnnotatedString())