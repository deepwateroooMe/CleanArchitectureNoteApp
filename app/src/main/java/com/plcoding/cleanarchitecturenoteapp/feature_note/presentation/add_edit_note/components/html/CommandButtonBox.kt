package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.html

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.solver.widgets.Rectangle

@Preview
@Composable
fun ExampleBox(
    // shape: Shape,
    // text: String
){
    Column(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center)) {
         Box(
             modifier = Modifier.size(100.dp).clip(RectangleShape).background(Color.Red)
         )
        Text("me")
    }
}