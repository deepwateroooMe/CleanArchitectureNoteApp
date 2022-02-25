package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.html

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.text.HtmlCompat

/**
 * Load a styled string resource with formatting.
 *
 * @param id the resource identifier
 * @param formatArgs the format arguments
 * @return the string data associated with the resource
 */
// 这里annotatedString到html span是需要一步转化的
// <string name="annotated_text">This is a string containing an <annotation format="bold">annotation</annotation> which we can use to <annotation format="italic">style</annotation> the substrings</string>

@SuppressLint("RememberReturnType") // 我这里好像理解得不对，
@Composable
 fun annotatedStringResource(@StringRes id: Int, vararg formatArgs: Any): AnnotatedString {
//fun annotatedStringResource(text: String, vararg formatArgs: Any?): AnnotatedString {
    val TAG = "test annotatedStringResource"
    Log.d(TAG, "annotatedStringResource")
     val text = stringResource(id, *formatArgs)
    Log.d(TAG, "text: " + text)
    val spanned = remember(text) {
        HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
    Log.d(TAG, "spanned.value: " + spanned.toString())

    return remember(spanned) {
        buildAnnotatedString {
            append(spanned.toString())
            Log.d(TAG, "spanned.toString(): " + spanned.toString())
            Log.d(TAG, "spanned.getSpans(0, spanned.length, Any::class.java).length: " + spanned.getSpans(0, spanned.length, Any::class.java).size)
            // 这下面的不对，得不到想要的标签
            spanned.getSpans(0, spanned.length, Any::class.java).forEach {
                span ->
                    val start = spanned.getSpanStart(span)
                    val end = spanned.getSpanEnd(span)
                    Log.d(TAG, "(span == null): " + (span == null))
                    when (span) {
                        is StyleSpan -> when (span.style) {
                            Typeface.BOLD -> {
                                Log.d(TAG, "Typeface.BOLD")
                                addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
                            }
                            Typeface.ITALIC ->
                                addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
                            Typeface.BOLD_ITALIC ->
                                addStyle(
                                    SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontStyle = FontStyle.Italic,
                                    ),
                                    start,
                                    end,
                                )
                        }
                        is UnderlineSpan ->
                            addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)
                        is ForegroundColorSpan ->
                            addStyle(SpanStyle(color = Color(span.foregroundColor)), start, end)
                    }
            }
        }
    }
}