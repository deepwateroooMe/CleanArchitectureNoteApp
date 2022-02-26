package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.html

import android.graphics.Typeface
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@Composable
// For now i'm using an AndroidView with a TextView inside. Not the best solution, but it's simple and that solves the problem. simple tests works
fun HtmlText(html: String, modifier: Modifier = Modifier) {

    AndroidView( // 我不想用这个来着。。。。。。，可能先测试一下这个思路框架吧
                 modifier = modifier,
                 factory = { context -> TextView(context) },
                 update = { it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT) }
    )
    // If you have tags in the HTML you need to set the TextView property movementMethod = LinkMovementMethod.getInstance() to make the links clickable.
}

// 这个可以运行：supports links
@Composable
fun HtmlText(
    html: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    hyperlinkStyle: TextStyle = TextStyle.Default,
    softWrap: Boolean = true,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    onHyperlinkClick: (uri: String) -> Unit = {}
) {
    val spanned = remember(html) {
        HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY, null, null)
    }

    val annotatedText = remember(spanned, hyperlinkStyle) {
        buildAnnotatedString {
            append(spanned.toString())
            spanned.getSpans(0, spanned.length, Any::class.java).forEach {
                span ->
                    val startIndex = spanned.getSpanStart(span)
                val endIndex = spanned.getSpanEnd(span)
                when (span) {
                    is StyleSpan -> {
                        span.toSpanStyle()?.let {
                            addStyle(style = it, start = startIndex, end = endIndex)
                        }
                    }
                    is UnderlineSpan -> {
                        addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start = startIndex, end = endIndex)
                    }
                    is URLSpan -> {
                        addStyle(style = hyperlinkStyle.toSpanStyle(), start = startIndex, end = endIndex)
                        addStringAnnotation(tag = Tag.Hyperlink.name, annotation = span.url, start = startIndex, end = endIndex)
                    }
                }
            }
        }
    }
    // 这个是针对整个文本的，可能针对link并不合适，最好是：需要另想办法 https://zhuanlan.zhihu.com/p/369109654
    ClickableText(
        annotatedText,
        modifier = modifier,
        style = style,
        softWrap = softWrap,
        overflow = overflow,
        maxLines = maxLines,
        onTextLayout = onTextLayout
    ) {
        annotatedText.getStringAnnotations(tag = Tag.Hyperlink.name, start = it, end = it).firstOrNull()?.let {
            onHyperlinkClick(it.item)
        }
    }
}

private fun StyleSpan.toSpanStyle(): SpanStyle? {
    return when (style) {
        Typeface.BOLD -> SpanStyle(fontWeight = FontWeight.Bold)
        Typeface.ITALIC -> SpanStyle(fontStyle = FontStyle.Italic)
        Typeface.BOLD_ITALIC -> SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)
        else -> null
    }
}

private enum class Tag {
    Hyperlink
}