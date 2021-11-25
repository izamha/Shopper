package com.izamha.snacky.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

fun snackyToast(
    context: Context,
    text: String,
    duration: Int = Toast.LENGTH_LONG,
) {
    Toast.makeText(context, text, duration).show()
}