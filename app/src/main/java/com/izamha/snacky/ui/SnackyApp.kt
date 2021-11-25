package com.izamha.snacky.ui

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.izamha.snacky.ui.snackdetail.SnackDetail
import com.izamha.snacky.ui.theme.SnackyTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.izamha.snacky.repository.callToRoom
import com.izamha.snacky.ui.components.*
import com.izamha.snacky.ui.home.*
import com.izamha.snacky.viewmodel.SnackViewModel

@ExperimentalPermissionsApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun SnackyApp(snackViewModel: SnackViewModel) {
    ProvideWindowInsets {
        SnackyTheme {
            val appStateHolder = rememberAppStateHolder()
            SnackyScaffold(
                bottomBar = {
                    if (appStateHolder.shouldShowBottomBar) {
                        SnackyBottomBar(
                            tabs = appStateHolder.bottomBarTabs,
                            currentRoute = appStateHolder.currentRoute!!,
                            navigateToRoute = appStateHolder::navigateToBottomBarRoute
                        )
                    }
                },
                snackbarHost = {
                    SnackbarHost(
                        hostState = it,
                        modifier = Modifier.systemBarsPadding(),
                        snackbar = { snackbarData -> ShopperSnackbar(snackbarData) }
                    )
                },
                scaffoldState = appStateHolder.scaffoldState
            ) { innerPaddingModifier ->
                NavHost(
                    navController = appStateHolder.navController,
                    startDestination = MainDestinations.HOME_ROUTE,
                    modifier = Modifier.padding(innerPaddingModifier)
                ) {
                    try {
                        snackyNavGraph(
                            onSnackSelected = appStateHolder::navigateToSnackDetail,
                            upPress = appStateHolder::upPress,
                            snackViewModel = snackViewModel
                        )
                    } catch (e: Exception) {
                        Log.d("SNACK_ID", "SnackyApp: ${e.cause}")
                    }

                    composable("home/somewhere") {
                        SomeWhere(navController = appStateHolder.navController)
                    }

                }

            }
        }
    }
}

@ExperimentalPermissionsApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
private fun NavGraphBuilder.snackyNavGraph(
    onSnackSelected: (Long, NavBackStackEntry) -> Unit,
    upPress: () -> Unit,
    snackViewModel: SnackViewModel
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = HomeSections.FEED.route
    ) {
        addHomeGraph(
            onSnackSelected = onSnackSelected,
            snackViewModel = snackViewModel
        )
    }
    composable(
        "${MainDestinations.SNACK_DETAIL_ROUTE}/{${MainDestinations.SNACK_ID_KEY}}",
        arguments = listOf(navArgument(MainDestinations.SNACK_ID_KEY) { type = NavType.LongType })
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val snackId = arguments.getLong(MainDestinations.SNACK_ID_KEY)
        SnackDetail(snackId, upPress, snackViewModel)
    }

}


@Composable
fun SomeWhere(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberAppStateHolder().navController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "You are SomeWhere...", fontSize = 32.sp, color = Color.Gray)
        SnackyDivider(thickness = 3.dp, modifier = modifier.padding(16.dp))
        SnackyButton(
            onClick = { navController.navigate("home/feed") },
            modifier = modifier.padding(16.dp)
        ) {
            Text(text = "GO HOME", modifier = modifier.padding(16.dp))
        }
    }
}
