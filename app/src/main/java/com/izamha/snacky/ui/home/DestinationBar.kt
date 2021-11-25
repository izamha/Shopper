package com.izamha.snacky.ui.home

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.izamha.snacky.R
import com.izamha.snacky.ui.components.SnackyDivider
import com.izamha.snacky.ui.theme.AlphaNearOpaque
import com.izamha.snacky.ui.theme.SnackyTheme
import com.google.accompanist.insets.statusBarsPadding

@ExperimentalAnimationApi
@Composable
fun DestinationBar() {

    val context = LocalContext.current

    var locationVisible by rememberSaveable { mutableStateOf(false) }

    InsideDestinationBar(onClick = {
        locationVisible = true
    })

    AnimatedVisibility(
        visible = locationVisible,
        enter = slideInVertically() + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        LocationsScreen { locationVisible = false }
    }
}

@Composable
fun InsideDestinationBar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Column(modifier = modifier.statusBarsPadding()) {
        TopAppBar(
            backgroundColor = SnackyTheme.colors.uiBackground.copy(alpha = AlphaNearOpaque),
            contentColor = SnackyTheme.colors.textSecondary,
            elevation = 0.dp
        ) {
            Text(
                text = "Ku Bisima, KN 112St",
                style = MaterialTheme.typography.subtitle1,
                color = SnackyTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            IconButton(
                onClick = onClick,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ExpandMore,
                    tint = SnackyTheme.colors.brand,
                    contentDescription = stringResource(R.string.label_select_delivery)
                )
            }
        }
        SnackyDivider()
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
fun PreviewDestinationBar() {
    SnackyTheme {
        // DestinationBar()
    }
}
