package com.izamha.snacky.ui.home

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.izamha.snacky.R
import com.izamha.snacky.model.Filter
import com.izamha.snacky.model.SnackRepo
import com.izamha.snacky.model.destinations
import com.izamha.snacky.ui.components.FilterChip
import com.izamha.snacky.ui.components.SnackyScaffold
import com.izamha.snacky.ui.components.SnackyButton
import com.izamha.snacky.ui.theme.SnackyTheme
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.izamha.snacky.model.Emoji
import com.izamha.snacky.ui.components.snackyToast


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LocationsScreen(
    onDismiss: () -> Unit
) {
    var sortState by remember { mutableStateOf(SnackRepo.getSortDefault()) }
    var maxCalories by remember { mutableStateOf(0f) }
    val defaultFilter = SnackRepo.getSortDefault()
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        val priceFilters = remember { SnackRepo.getPriceFilters() }
        val addDestination = remember { SnackRepo.getAddDestination() }
        SnackyScaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(id = R.string.close)
                            )
                        }
                    },
                    title = {
                        Text(
                            text = stringResource(id = R.string.label_destinations),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h6
                        )
                    },
                    actions = {
                        var resetEnabled = sortState != defaultFilter
                        IconButton(
                            onClick = {
                                /* TODO: Open search */
                                Toast.makeText(
                                    context,
                                    "Everything is reset now!",
                                    Toast.LENGTH_LONG
                                ).show()
                            },
                            enabled = resetEnabled
                        ) {
                            val alpha = if (resetEnabled) {
                                ContentAlpha.high
                            } else {
                                ContentAlpha.disabled
                            }
                            CompositionLocalProvider(LocalContentAlpha provides alpha) {
                                Text(
                                    text = stringResource(id = R.string.reset),
                                    style = MaterialTheme.typography.body2
                                )
                            }
                        }
                    },
                    backgroundColor = SnackyTheme.colors.uiBackground
                )
            }
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp),
            ) {

                PrimaryDestination(
                    sortState = sortState,
                    onFilterChange = { filter ->
                        sortState = filter.name
                    }
                )

                SecondaryDestination(
                    title = stringResource(id = R.string.label_secondary_destination),
                    filters = destinations
                )
                SnackyButton(
                    onClick = {
                        snackyToast(context, "Work in progress...")
                    },
                    modifier = Modifier
                        .padding(24.dp)
                        .align(Alignment.CenterHorizontally),
                ) {
                    Text(
                        text = "ADD A DESTINATION",
                        modifier = Modifier.wrapContentSize(),
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }

                Urgency(
                    sliderPosition = maxCalories,
                    onValueChanged = { newValue ->
                        maxCalories = newValue
                    },
                )
            }
        }
    }
}


@Composable
fun Urgency(
    modifier: Modifier = Modifier,
    sliderPosition: Float,
    onValueChanged: (Float) -> Unit,
) {
    val context = LocalContext.current
    var howHungry by remember { mutableStateOf(0f) }

    FlowRow {
        FilterTitle(text = stringResource(id = R.string.label_hungermeter))
        Text(
            text = stringResource(id = R.string.label_how_hungry),
            style = MaterialTheme.typography.body2,
            color = SnackyTheme.colors.brand,
            modifier = Modifier.padding(top = 5.dp, start = 10.dp)
        )
    }
    Slider(
        value = sliderPosition,
        onValueChange = { newValue ->
            onValueChanged(newValue)
            howHungry = newValue
        },
        valueRange = 0f..300f,
        steps = 5,
        modifier = Modifier
            .fillMaxWidth(),
        colors = SliderDefaults.colors(
            thumbColor = SnackyTheme.colors.brand,
            activeTrackColor = SnackyTheme.colors.brand
        )
    )
    Spacer(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    )
    HungerMeter(modifier = modifier, howHungry = howHungry)
}

@Composable
fun HungerMeter(
    modifier: Modifier,
    howHungry: Float,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (howHungry) {
            0f -> {
                Text(text = Emoji.smiley, fontSize = 24.sp)
                Text(text = "Ntabwo nshonje.", fontSize = 24.sp)
            }
            50f -> {
                Text(text = Emoji.alien, fontSize = 24.sp)
                Text(text = "Ntabwo nshonje cyane.", fontSize = 24.sp)
            }
            100f -> {
                Text(text = Emoji.alien, fontSize = 24.sp)
                Text(text = "Ceceka! Byakaze.", fontSize = 24.sp)
            }
            150f -> {
                Text(text = Emoji.angryFace, fontSize = 24.sp)
                Text(text = "Ndashonje!", fontSize = 24.sp)
            }
            200f -> {
                Text(text = Emoji.funnyFace, fontSize = 24.sp)
                Text(text = "Ndashonje!", fontSize = 24.sp)
            }
            250f -> {
                Text(text = Emoji.angryFace, fontSize = 24.sp)
                Text(text = "Ndashonje cyane!", fontSize = 24.sp)
            }
            300f -> {
                Text(text = Emoji.brokenHeart, fontSize = 24.sp)
                Text(text = "Nenda gupfa!", fontSize = 24.sp)
            }
        }
    }
}


@Composable
fun SecondaryDestination(title: String, filters: List<Filter>) {
    FilterTitle(text = title)
    FlowRow(
        mainAxisAlignment = FlowMainAxisAlignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 16.dp)
            .padding(horizontal = 4.dp)
    ) {
        filters.forEach { filter ->
            FilterChip(
                filter = filter,
                modifier = Modifier.padding(end = 4.dp, bottom = 8.dp)
            )
        }
    }
}

@Composable
fun PrimaryDestination(sortState: String, onFilterChange: (Filter) -> Unit) {
    FilterTitle(text = stringResource(id = R.string.label_primary_destination))
    Column(Modifier.padding(bottom = 24.dp)) {
        PrimaryDestinations(
            sortState = sortState,
            onChanged = onFilterChange
        )
    }
}

@Composable
fun PrimaryDestinations(
    destinations: List<Filter> = SnackRepo.getDestinations(),
    sortState: String,
    onChanged: (Filter) -> Unit
) {

    destinations.forEach { destination ->
        DestinationOption(
            text = destination.name,
            icon = destination.icon,
            selected = sortState == destination.name,
            onClickOption = {
                onChanged(destination)
            }
        )
    }
}

@Composable
fun DestinationOption(
    text: String,
    icon: ImageVector?,
    onClickOption: () -> Unit,
    selected: Boolean
) {
    Row(
        modifier = Modifier
            .padding(top = 14.dp)
            .selectable(selected) { onClickOption() }
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null)
        }
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f)
        )
        if (selected) {
            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = null,
                tint = SnackyTheme.colors.brand
            )
        }
    }
}

