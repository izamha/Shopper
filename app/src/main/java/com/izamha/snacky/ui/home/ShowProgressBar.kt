package com.izamha.snacky.ui.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.izamha.snacky.model.Filter
import com.izamha.snacky.ui.components.HorizontalProgress

@Composable
fun ShowProgressBar(
    isVisible: Boolean,
    filter: Filter,
) {
    val selected = filter.enabled.value
    val context = LocalContext.current

    if (isVisible && selected) {
        Row(
            modifier = Modifier.padding(6.dp),
        ) {
//            Toast.makeText(
//                context, "HorizontalDots running now...",
//                Toast.LENGTH_SHORT
//            ).show()
            HorizontalProgress()
        }
    }
}