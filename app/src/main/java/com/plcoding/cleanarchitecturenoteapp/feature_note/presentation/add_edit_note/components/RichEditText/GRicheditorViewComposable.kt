package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.RichEditText

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.view.isInvisible
import com.plcoding.cleanarchitecturenoteapp.databinding.RichEditorLayoutBinding

val imgURL: String = "https://avatar.csdnimg.cn/1/9/7/1_qq_43143981_1552988521.jpg"

@SuppressLint("UnrememberedMutableState")
@Composable
fun GRicheditorViewComposable () {
    var isChangedTextColor = remember { mutableStateOf(false) }
    var isChangedBgColor = remember { mutableStateOf(false) }

    AndroidViewBinding(RichEditorLayoutBinding::inflate) {
        // 应该还是需要处理它的获取焦点事件
//        viewTreeObserver.addOnGlobalFocusChangeListener { _, _ -> richBtns.isInvisible = hasFocus()} //  setVisible(hasFocus())
        actionUndo.setOnClickListener() { mEditor.undo() }
        actionRedo.setOnClickListener() { mEditor.redo() }
        actionBold.setOnClickListener() { mEditor.setBold() }
        actionItalic.setOnClickListener() { mEditor.setItalic() }
        actionSubscript.setOnClickListener() { mEditor.setSubscript() }
        actionSuperscript.setOnClickListener() { mEditor.setSuperscript() }
        actionStrikethrough.setOnClickListener() { mEditor.setStrikeThrough() }
        actionUnderline.setOnClickListener() { mEditor.setUnderline() }

        actionHeading1.setOnClickListener() { mEditor.setHeading(1) }
        actionHeading2.setOnClickListener() { mEditor.setHeading(2) }
        actionHeading3.setOnClickListener() { mEditor.setHeading(3) }
        actionHeading4.setOnClickListener() { mEditor.setHeading(4) }
        actionHeading5.setOnClickListener() { mEditor.setHeading(5) }
        actionHeading6.setOnClickListener() { mEditor.setHeading(6) }
        actionIndent.setOnClickListener() { mEditor.setIndent() }
        actionOutdent.setOnClickListener() { mEditor.setOutdent() }
        actionAlignLeft.setOnClickListener() { mEditor.setAlignLeft() }
        actionAlignCenter.setOnClickListener() { mEditor.setAlignCenter() }
        actionAlignRight.setOnClickListener() { mEditor.setAlignRight() }
        actionInsertBullets.setOnClickListener() { mEditor.setBullets() }
        actionInsertNumbers.setOnClickListener() { mEditor.setNumbers() }
        actionBlockquote.setOnClickListener() { mEditor.setBlockquote() }
        actionInsertAudio.setOnClickListener() { mEditor.insertAudio("https://file-examples-com.github.io/uploads/2017/11/fileexampleMP35MG.mp3") }
        actionInsertVideo.setOnClickListener() {
            mEditor.insertVideo(
                "https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_10MB.mp4",
                360
            )
        }
        actionInsertLink.setOnClickListener() { mEditor.insertLink("https://github.com/G452", "G") }
        actionInsertCheckbox.setOnClickListener() { mEditor.insertTodo() }
        actionInsertImage.setOnClickListener() { mEditor.insertImage(imgURL, "", 320) }
        actionTxtColor.setOnClickListener() {
            mEditor.setTextColor(if (isChangedTextColor.value) Color.BLACK else Color.RED)
            isChangedTextColor.value = !isChangedTextColor.value
        }
        actionBgColor.setOnClickListener() {
            mEditor.setTextBackgroundColor(if (isChangedBgColor.value) Color.TRANSPARENT else Color.YELLOW)
            isChangedBgColor.value = !isChangedBgColor.value
        }

        mEditor.setPlaceholder("开始你的创作")
        mEditor.setPadding(8, 8, 8, 8)
        // setEditPadding(16, 16, 16, 16)
    }
}