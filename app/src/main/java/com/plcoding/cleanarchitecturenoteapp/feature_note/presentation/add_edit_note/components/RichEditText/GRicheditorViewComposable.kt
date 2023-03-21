package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.RichEditText

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.focusable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.plcoding.cleanarchitecturenoteapp.databinding.RichEditorLayoutBinding
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.AddEditNoteEvent
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.AddEditNoteViewModel
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.EMPTY_IMAGE_URI
import jp.wasabeef.richeditor.RichEditor.OnTextChangeListener

val imgURL: String = "https://avatar.csdnimg.cn/1/9/7/1_qq_43143981_1552988521.jpg"

 @Composable
 fun GRicheditorViewComposable (
     modifier: Modifier,
     color: Int,
     content: String
     // onFocusChange: (FocusState) -> Unit
 ) {
     AndroidViewBinding(RichEditorLayoutBinding::inflate) {
         mEditor.setEditorHeight(100)
         mEditor.setEditorBackgroundColor(color)
         mEditor.setHtml(content)
         // 这里只取其富文本显示功能，不可编辑，但点击事件可以设置得更为智能一点儿，软键盘很难用
         mEditor.setInputEnabled(false)
     }
 }
 
@SuppressLint("UnrememberedMutableState")
@Composable
fun GRicheditorViewComposable (
    modifier: Modifier,
    color: Int,
    viewModel: AddEditNoteViewModel,
    // onFocusChange: (FocusState) -> Unit
) {
    val TAG = "test GRicheditorViewComposable"

    var isChangedTextColor = remember { mutableStateOf(false) }
    var isChangedBgColor = remember { mutableStateOf(false) }
    
    AndroidViewBinding(RichEditorLayoutBinding::inflate) {
        mEditor.setEditorBackgroundColor(color)
        mEditor.setHtml(viewModel.noteContent.value.text)
        mEditor.setOnTextChangeListener() {
            viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
            // BUG #2: 这里可以如此设置显示，稍显笨拙；可是要如何设置隐藏呢？不知道该如何自动检测聚焦事件
            // 这里的事件传递方向，应该是自这个composable向外传递
            if (!richBtns.isVisible) richBtns.isVisible = true
            // mEditor.focusEditor() mEditor.clearFocusEditor() 不知道怎么用
        }
        // 这些按钮的显示与否，可以再调控一下, hasFocus() == visible 这里目前还没有设置对
        // viewTreeObserver.addOnGlobalFocusChangeListener { _, _ -> richBtns.setVisible(hasFocus()) } // 安卓原生view里的方法
//         modifier.onFocusChanged { focusState ->
//             when {
//                 focusState.isFocused -> {
//                     Log.d(TAG, "isFocused")
//                     richBtns.isVisible = true
//                 }
// //                focusState.hasFocus ->
// //                    Log.d(TAG, "hasFocus")
// //                    richBtns.isVisible = true
//                     // println("A child of mine has focus!")
//             }
//         }
// modifier.onFocusEvent() {
//     Log.d(TAG, "onFocusEvent")
//             richBtns.isVisible = true
//         }

        actionUndo.setOnClickListener() {
            mEditor.undo()
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionRedo.setOnClickListener() {
            mEditor.redo()
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionBold.setOnClickListener() {
            mEditor.setBold()
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionItalic.setOnClickListener() {
            mEditor.setItalic()
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionSubscript.setOnClickListener() {
            mEditor.setSubscript()
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionSuperscript.setOnClickListener() {
            mEditor.setSuperscript()
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionStrikethrough.setOnClickListener() {
            mEditor.setStrikeThrough()
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionUnderline.setOnClickListener() {
            mEditor.setUnderline()
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionHeading1.setOnClickListener() {
            mEditor.setHeading(1)
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionHeading2.setOnClickListener() {
            mEditor.setHeading(2)
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionHeading3.setOnClickListener() {
            mEditor.setHeading(3)
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionHeading4.setOnClickListener() {
            mEditor.setHeading(4)
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionHeading5.setOnClickListener() {
            mEditor.setHeading(5)
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionHeading6.setOnClickListener() {
            mEditor.setHeading(6)
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionIndent.setOnClickListener() {
            mEditor.setIndent()
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionOutdent.setOnClickListener() {
            mEditor.setOutdent()
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionAlignLeft.setOnClickListener() {
            mEditor.setAlignLeft()
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionAlignCenter.setOnClickListener() {
            mEditor.setAlignCenter()
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionAlignRight.setOnClickListener() {
            mEditor.setAlignRight()
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionInsertBullets.setOnClickListener() {
            mEditor.setBullets()
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionInsertNumbers.setOnClickListener() {
            mEditor.setNumbers()
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionBlockquote.setOnClickListener() {
            mEditor.setBlockquote()
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionInsertImage.setOnClickListener() {
            val url = "https://nyc3.digitaloceanspaces.com/food2fork/food2fork-static/featured_images/500/featured_image.png"
            viewModel.onEvent(AddEditNoteEvent.LoadImageUrl(url))
            mEditor.insertImage(viewModel.noteImage.value.url, "", 150)
        }
        actionInsertAudio.setOnClickListener() {
            mEditor.insertAudio("https://file-examples-com.github.io/uploads/2017/11/fileexampleMP35MG.mp3")
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionInsertVideo.setOnClickListener() {
            mEditor.insertVideo(
                "https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_10MB.mp4",
                360
            )
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionInsertLink.setOnClickListener() {
            mEditor.insertLink("https://github.com/G452", "G")
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionInsertCheckbox.setOnClickListener() {
            mEditor.insertTodo()
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionTxtColor.setOnClickListener() {
            mEditor.setTextColor(if (isChangedTextColor.value) Color.BLACK else Color.RED)
            isChangedTextColor.value = !isChangedTextColor.value
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }
        actionBgColor.setOnClickListener() {
            mEditor.setTextBackgroundColor(if (isChangedBgColor.value) Color.TRANSPARENT else Color.YELLOW)
            isChangedBgColor.value = !isChangedBgColor.value
            viewModel.onEvent((AddEditNoteEvent.EnteredContent(mEditor.html)))
        }

        mEditor.setPlaceholder("Enter some content...")
        mEditor.setPadding(8, 8, 8, 8)
    }
}