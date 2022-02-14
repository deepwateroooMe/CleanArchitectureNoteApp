package com.plcoding.cleanarchitecturenoteapp.ui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.plcoding.cleanarchitecturenoteapp.R


@Composable
fun BackButton(onBackPress: () -> Unit) {
    IconButton(
        onClick = {
            onBackPress()
        }
    ) {
        Icon(
            Icons.Filled.ArrowBack,
            tint = MaterialTheme.colors.onPrimary,
            contentDescription = stringResource(id = R.string.content_description_back_button)
        )
    }
}
