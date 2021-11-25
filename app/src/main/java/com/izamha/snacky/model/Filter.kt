package com.izamha.snacky.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector

@Stable
class Filter(
    val name: String,
    enabled: Boolean = false,
    val icon: ImageVector? = null
) {
    val enabled = mutableStateOf(enabled)
}
val filters = listOf(
    Filter(name = "Organic"),
    Filter(name = "Gluten-free"),
    Filter(name = "Dairy-free"),
    Filter(name = "Sweet"),
    Filter(name = "Greenish")
)
val priceFilters = listOf(
    Filter(name = "100"),
    Filter(name = "500"),
    Filter(name = "1000"),
    Filter(name = "2000"),
    Filter(name = "5000")
)
val sortFilters = listOf(
    Filter(name = "My favorite (default)", icon = Icons.Filled.Android),
    Filter(name = "Rating", icon = Icons.Filled.Star),
    Filter(name = "Alphabetical", icon = Icons.Filled.SortByAlpha)
)

val destinations = listOf(
    Filter(name = "Ku Bisima, KN 122St", icon = Icons.Filled.Map),
    Filter(name = "Biryogo, KN 100St", icon = Icons.Filled.MapsHomeWork),
    Filter(name = "Rwampara Health Center", icon = Icons.Filled.MapsUgc)
)

val categoryFilters = listOf(
    Filter(name = "Chips & crackers"),
    Filter(name = "Fruit snacks"),
    Filter(name = "Desserts"),
    Filter(name = "Nuts")
)

val addDestination = listOf(
    Filter("Add a Destination")
)

val lifeStyleFilters = listOf(
    Filter(name = "Organic"),
    Filter(name = "Gluten-free"),
    Filter(name = "Dairy-free"),
    Filter(name = "Sweet"),
    Filter(name = "Greenish")
)

var sortDefault = sortFilters[0].name
