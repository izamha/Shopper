package com.izamha.snacky.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.izamha.snacky.R
import com.izamha.snacky.ui.theme.Shapes

@ExperimentalMaterialApi
@Composable
fun FlexibleButton(
    modifier: Modifier = Modifier,
    text: String = "Sign Up with Google",
    leadingText: String = "Kwinjira...",
    icon: Painter = painterResource(id = R.drawable.ic_google_logo),
    shape: Shape = Shapes.large,
    borderColor: Color = Color.LightGray,
    backgroundColor: Color = MaterialTheme.colors.surface,
    progressIndicatorColor: Color = MaterialTheme.colors.primary,
    onClicked: () -> Unit
) {
    var clicked by remember { mutableStateOf(false) }

    Surface(
        onClick = { clicked = !clicked },
        shape = shape,
        border = BorderStroke(width = 1.dp, color = borderColor),
        color = backgroundColor,
    ) {
        Row(
            modifier = modifier
                .padding(
                    start = 12.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp
                )
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Text(text = if (clicked) leadingText else text)
            if (clicked) {
                Spacer(modifier = Modifier.width(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(16.dp)
                        .width(16.dp),
                    strokeWidth = 2.dp,
                    color = progressIndicatorColor
                )
                onClicked()
            }
        }
    }
}


@ExperimentalMaterialApi
@Composable
fun FlexibleButton2(
    modifier: Modifier = Modifier,
    text: String = "Sign Up with Google",
    leadingText: String = "Kwinjira...",
    icon: Painter = painterResource(id = R.drawable.ic_google_logo),
    shape: Shape = Shapes.large,
    borderColor: Color = Color.LightGray,
    backgroundColor: Color = MaterialTheme.colors.surface,
    progressIndicatorColor: Color = MaterialTheme.colors.primary,
    onClicked: () -> Unit
) {
    var clicked by remember { mutableStateOf(false) }

    Surface(
        onClick = { clicked = !clicked },
        shape = shape,
        border = BorderStroke(width = 1.dp, color = borderColor),
        color = backgroundColor,
    ) {
        Row(
            modifier = modifier
                .padding(
                    start = 12.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp
                )
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Text(text = if (clicked) leadingText else text)
            if (clicked) {
                Spacer(modifier = Modifier.width(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(16.dp)
                        .width(16.dp),
                    strokeWidth = 2.dp,
                    color = progressIndicatorColor
                )
                onClicked()
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
@Preview
private fun GoogleButtonPreview() {
    FlexibleButton(
        onClicked = {},
    )
}