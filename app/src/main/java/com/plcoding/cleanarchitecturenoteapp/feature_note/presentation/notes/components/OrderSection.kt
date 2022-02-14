package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.NoteOrder
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.OrderType

//@Preview
@Composable // 排序toggle显示的排序选项界面
fun OrderSection (
    modifier: Modifier = Modifier,
    noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    onOrderChange: (NoteOrder) -> Unit
) {
    // 一列三行：文本，日期和颜色
    Column(
        // modifier = modifier
            modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Ascending",
                selected = noteOrder.orderType is OrderType.Ascending,
                onSelected = { onOrderChange(noteOrder.copy(OrderType.Ascending)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Desending",
                selected = noteOrder.orderType is OrderType.Descending,
                onSelected = { onOrderChange(noteOrder.copy(OrderType.Descending)) }
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        DefaultRadioButton(
            text = "Text",
            selected = noteOrder is NoteOrder.Title,
            onSelected = { onOrderChange(NoteOrder.Title(noteOrder.orderType)) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        DefaultRadioButton(
            text = "Date",
            selected = noteOrder is NoteOrder.Title,
            onSelected = { onOrderChange(NoteOrder.Date(noteOrder.orderType)) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        DefaultRadioButton(
            text = "Color",
            selected = noteOrder is NoteOrder.Title,
            onSelected = { onOrderChange(NoteOrder.Color(noteOrder.orderType)) }
        )
    }
}