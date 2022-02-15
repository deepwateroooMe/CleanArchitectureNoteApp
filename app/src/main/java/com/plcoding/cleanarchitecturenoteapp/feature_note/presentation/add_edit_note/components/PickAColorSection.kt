package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.me.colorpicker.ClassicColorPicker
import com.me.colorpicker.HsvColor
import com.plcoding.cleanarchitecturenoteapp.R

@Composable // 排序toggle显示的排序选项界面
fun PickAColorSection (
    modifier: Modifier = Modifier,
    noteColor: Color = Color.White,
    onColorChange: (Color) -> Unit
) {
  val TAG = "test PickAColorSection"

//  val scope = rememberCoroutineScope()

    Column {
//         // Classic Color Picker
//         TopAppBar(
//             title = {
//                 Text(stringResource(R.string.classic_color_picker_sample))
//             },
// //            navigationIcon = {
// //                BackButton { navController.navigateUp() }
// //            }
//         )
        // 值：可变值，当前选定的颜色,这里的设置把之前的值覆盖掉了,应该是自己的手机上设置了夜间模式，所以主页面背景变黑了，再搜一搜，弄确信一下
        val currentColor = remember {
            mutableStateOf(Color.White)
        }

        // // 当前选定颜色的界面，圆形
        // ColorPreviewInfo(currentColor = currentColor.value)
        
        ClassicColorPicker(
            color = currentColor.value,
            modifier = Modifier
                .height(300.dp)
                .padding(16.dp),
            onColorChanged = {
                    hsvColor: HsvColor ->
                        //                                   // Triggered when the color changes, do something with the newly picked color here!
                    Log.d(TAG, "onColorChanged()")
                               currentColor.value = hsvColor.toColor()
                               onColorChange(currentColor.value)
                               
            }
        )
    }
}