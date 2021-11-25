package com.izamha.snacky.ui.home

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.izamha.snacky.model.Filter
import com.izamha.snacky.model.SnackCollection
import com.izamha.snacky.model.SnackRepo
import com.izamha.snacky.ui.components.*
import com.izamha.snacky.ui.theme.SnackyTheme
import com.google.accompanist.insets.statusBarsHeight
import com.google.firebase.auth.FirebaseAuth
import com.izamha.snacky.viewmodel.SnackViewModel

@ExperimentalCoilApi
@ExperimentalAnimationApi
@Composable
fun Feed(
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    snackViewModel: SnackViewModel
) {

    val mAuth = FirebaseAuth.getInstance()
    val currentUser = mAuth.currentUser

    val snacksCollValue = SnackRepo.getSnacks(snackViewModel = snackViewModel)
    val snackCollections = remember { snacksCollValue }
    val filters = remember { SnackRepo.getFilters() }

    // Log.d("FEED", "Feed: ${snackCollections[0].snacks.size}")

    if (snackCollections[0].snacks.isEmpty()) {
        // Show Static Stuff
        val snacksCollValueStatic = SnackRepo.getSnacksStatic()
        val snackCollectionsStatic = remember { snacksCollValueStatic }
        Feed(
            snackCollections = snackCollectionsStatic,
            filters = filters,
            onSnackClick = onSnackClick,
            snackViewModel = snackViewModel,
            modifier = modifier
        )
    } else {
        Text(text = "SnackCollection Empty")
        Feed(
            snackCollections = snackCollections,
            filters = filters,
            onSnackClick = onSnackClick,
            snackViewModel = snackViewModel,
            modifier = modifier
        )
    }
}

@ExperimentalCoilApi
@ExperimentalAnimationApi
@Composable
private fun Feed(
    snackCollections: List<SnackCollection>,
    filters: List<Filter>,
    onSnackClick: (Long) -> Unit,
    snackViewModel: SnackViewModel,
    modifier: Modifier = Modifier
) {

    SnackySurface(modifier = modifier.fillMaxSize()) {
        Box {
            SnackCollectionList(
                snackCollections = snackCollections,
                filters = filters,
                onSnackClick = onSnackClick,
                snackViewModel = snackViewModel
            )
            DestinationBar()
        }
    }
}

@ExperimentalCoilApi
@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SnackCollectionList(
    snackCollections: List<SnackCollection>,
    filters: List<Filter>,
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    snackViewModel: SnackViewModel
) {
    var filtersVisible by rememberSaveable { mutableStateOf(false) }
    val allSnacks by snackViewModel.readAllData.observeAsState(listOf())
    val context = LocalContext.current

    Box(modifier) {

        LazyColumn {
            item {
                Spacer(Modifier.statusBarsHeight(additional = 56.dp))
                FilterBar(filters, onShowFilters = { filtersVisible = true })

                // HorizontalProgress()
                filters.forEach { filter ->
                    ShowProgressBar(isVisible = true, filter = filter)
                }

            }
            itemsIndexed(snackCollections) { index, snackCollection ->
                if (index > 0) {
                    SnackyDivider(thickness = 2.dp)
                }

                SnackCollection(
                    snackCollection = snackCollection,
                    onSnackClick = onSnackClick,
                    index = index
                )
            }
        }
    }
    AnimatedVisibility(
        visible = filtersVisible,
        enter = slideInVertically() + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        FilterScreen { filtersVisible = false }
    }
}


@ExperimentalAnimationApi
@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
fun HomePreview() {
    SnackyTheme {
        // Feed(onSnackClick = { })
    }
}
