package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isInvisible
import com.plcoding.cleanarchitecturenoteapp.R
import kotlinx.android.synthetic.main.rich_editor_layout.view.*

/**
 * author：G
**/
class GRicheditorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1
) : FrameLayout(context, attrs, defStyleAttr) {
    private val imgURL: String = "https://avatar.csdnimg.cn/1/9/7/1_qq_43143981_1552988521.jpg"

    init {
        LayoutInflater.from(context).inflate(R.layout.rich_editor_layout, this)
        viewTreeObserver.addOnGlobalFocusChangeListener { _, _ -> richBtns.isInvisible = hasFocus()} //  setVisible(hasFocus())
        initClick()
    }

// 这里需要改写一下：因为要传入一个总的点击回调，所以需要在这里内部来分清：点击的到底是哪个视图，并执行相应的方法
    private fun initClick() {
        var isChangedTextColor = false
        var isChangedBgColor = false
        action_undo.setOnClickListener() { mEditor.undo() }
        action_redo.setOnClickListener() { mEditor.redo() }
        action_bold.setOnClickListener() { mEditor.setBold() }
        action_italic.setOnClickListener() { mEditor.setItalic() }
        action_subscript.setOnClickListener() { mEditor.setSubscript() }
        action_superscript.setOnClickListener() { mEditor.setSuperscript() }
        action_strikethrough.setOnClickListener() { mEditor.setStrikeThrough() }
        action_underline.setOnClickListener() { mEditor.setUnderline() }
        action_heading1.setOnClickListener() { mEditor.setHeading(1) }
        action_heading2.setOnClickListener() { mEditor.setHeading(2) }
        action_heading3.setOnClickListener() { mEditor.setHeading(3) }
        action_heading4.setOnClickListener() { mEditor.setHeading(4) }
        action_heading5.setOnClickListener() { mEditor.setHeading(5) }
        action_heading6.setOnClickListener() { mEditor.setHeading(6) }
        action_indent.setOnClickListener() { mEditor.setIndent() }
        action_outdent.setOnClickListener() { mEditor.setOutdent() }
        action_align_left.setOnClickListener() { mEditor.setAlignLeft() }
        action_align_center.setOnClickListener() { mEditor.setAlignCenter() }
        action_align_right.setOnClickListener() { mEditor.setAlignRight() }
        action_insert_bullets.setOnClickListener() { mEditor.setBullets() }
        action_insert_numbers.setOnClickListener() { mEditor.setNumbers() }
        action_blockquote.setOnClickListener() { mEditor.setBlockquote() }
        action_insert_audio.setOnClickListener() { mEditor.insertAudio("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_5MG.mp3") }
        action_insert_video.setOnClickListener() {
            mEditor.insertVideo(
                "https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_10MB.mp4",
                360
            )
        }
        action_insert_link.setOnClickListener() { mEditor.insertLink("https://github.com/G452", "G") }
        action_insert_checkbox.setOnClickListener() { mEditor.insertTodo() }
        action_insert_image.setOnClickListener() { mEditor.insertImage(imgURL, "", 320) }
        action_txt_color.setOnClickListener() {
            mEditor.setTextColor(if (isChangedTextColor) Color.BLACK else Color.RED)
            isChangedTextColor = !isChangedTextColor
        }
        action_bg_color.setOnClickListener() {
            mEditor.setTextBackgroundColor(if (isChangedBgColor) Color.TRANSPARENT else Color.YELLOW)
            isChangedBgColor = !isChangedBgColor
        }
    }

    fun setEditPadding(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
        mEditor.setPadding(left, top, right, bottom)
    }

    fun setPlaceholder(text: Any) {
        mEditor.setPlaceholder(text.toString())
    }

    fun getHtml(): String? {
        return mEditor.html
    }
}

