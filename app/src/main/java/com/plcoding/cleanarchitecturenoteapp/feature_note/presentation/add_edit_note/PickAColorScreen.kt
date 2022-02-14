package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note

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
import androidx.navigation.NavController
import com.me.colorpicker.ClassicColorPicker
import com.me.colorpicker.HsvColor
import com.plcoding.cleanarchitecturenoteapp.R
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.ColorPreviewInfo
import com.plcoding.cleanarchitecturenoteapp.ui.BackButton
import com.plcoding.cleanarchitecturenoteapp.ui.ComposeColorPickerTheme

@Composable // 颜色拾取器
fun PickAColorScreen (
    navController: NavController
) {
    Column {
        // Classic Color Picker
        TopAppBar(
            title = {
                Text(stringResource(R.string.classic_color_picker_sample))
            },
            navigationIcon = {
                BackButton { navController.navigateUp() }
            }
        )
        // 值：可变值，当前选定的颜色
        val currentColor = remember {
            mutableStateOf(Color.Black)
        }
        // 当前选定颜色的界面，圆形
        ColorPreviewInfo(currentColor = currentColor.value)

        ClassicColorPicker(
            color = currentColor.value,
            modifier = Modifier
                .height(300.dp)
                .padding(16.dp),
            onColorChanged = { hsvColor: HsvColor ->
                                   // Triggered when the color changes, do something with the newly picked color here!
                               currentColor.value = hsvColor.toColor()
            }
        )
    }
}

@Composable
fun ClassicColorPickerPreview() {
    ComposeColorPickerTheme {
        ClassicColorPicker(
            modifier = Modifier.height(300.dp),
            color = Color.Green,
            onColorChanged = {
            }
        )
    }
}

@Composable
fun ClassicColorPickerNoAlphaPreview() {
    ComposeColorPickerTheme {
        ClassicColorPicker(
            modifier = Modifier.height(300.dp),
            color = Color.Magenta,
            showAlphaBar = false,
            onColorChanged = {
            }
        )
    }
}
