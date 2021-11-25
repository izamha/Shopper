package com.izamha.snacky.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.izamha.snacky.R
import com.izamha.snacky.ui.components.SnackyDivider

@Composable
fun SnackySplash() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 48.sp,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Loading...",
                fontSize = 16.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


@Preview
@Composable
fun SnackySplashPreview() {
    SnackySplash()
}