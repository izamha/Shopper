package com.izamha.snacky.repository

import android.app.Application
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.izamha.snacky.model.Snack
import androidx.compose.runtime.livedata.observeAsState
import com.izamha.snacky.ui.components.snackyToast
import com.izamha.snacky.viewmodel.SnackViewModel
import com.izamha.snacky.viewmodel.SnackViewModelFactory
import kotlin.Exception

@Composable
fun SaveToRoom(
    name: String,
    imageUrl: String,
    price: Long
) {
    val context = LocalContext.current
    val snackViewModel: SnackViewModel = viewModel(
        factory = SnackViewModelFactory(context.applicationContext as Application)
    )

    val snack = Snack(
        name = name,
        imageUrl = imageUrl,
        price = price
    )

    try {
        snackViewModel.createSnack(snack = snack)
        snackyToast(context = context, "Successfully saved to a local db!")
    } catch (e: Exception) {
        snackyToast(context, "${e.message}")
    }

}

@Composable
fun callToRoom(): List<Snack> {
    val context = LocalContext.current
    val snackViewModel: SnackViewModel = viewModel(
        factory = SnackViewModelFactory(context.applicationContext as Application)
    )
    return snackViewModel.readAllData.observeAsState(listOf()).value
}