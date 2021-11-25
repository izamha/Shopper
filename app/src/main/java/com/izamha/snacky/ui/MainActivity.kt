package com.izamha.snacky.ui

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.view.WindowCompat
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.auth.FirebaseAuth
import com.izamha.snacky.model.Snack
import com.izamha.snacky.ui.auth.SignInActivity
import com.izamha.snacky.viewmodel.SnackViewModel
import com.izamha.snacky.viewmodel.SnackViewModelFactory
import kotlinx.coroutines.delay

@ExperimentalPermissionsApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    private lateinit var mAuth: FirebaseAuth


    @ExperimentalCoilApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Initializing the viewModel
         * */

        val snackViewModel by viewModels<SnackViewModel> {
            SnackViewModelFactory((this.applicationContext as Application))
        }



        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        /**
         * If user is not authenticated, send them to SignInActivity to authenticate first.
         * Else send them to Feed
         * */

        // This app draws behind the system bars, so we want to handle fitting system windows
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            if (user != null) {
                roomData(snackViewModel = snackViewModel)
                SnackyApp(snackViewModel = snackViewModel)
            } else {
                val signInIntent = Intent(applicationContext, SignInActivity::class.java)
                startActivity(signInIntent)
                finish()
            }
        }

    }
}


@Composable
fun roomData(snackViewModel: SnackViewModel): List<Snack> {
    val allSnacks by snackViewModel.readAllData.observeAsState(listOf())
    if (allSnacks.isNotEmpty()) {
        Log.d("MAIN_ACTIVITY", "roomData: $allSnacks")
    } else {
        Log.d("MAIN_ACTIVITY", "STILL EMPTY: $allSnacks")
    }
    return allSnacks
}
