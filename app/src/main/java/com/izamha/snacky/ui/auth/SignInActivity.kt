package com.izamha.snacky.ui.auth

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.izamha.snacky.R
import com.izamha.snacky.ui.MainActivity
import com.izamha.snacky.ui.SnackyApp
import com.izamha.snacky.ui.components.FlexibleButton
import com.izamha.snacky.ui.components.SnackySurface
import com.izamha.snacky.ui.components.snackyToast
import com.izamha.snacky.ui.payment.InputItem
import com.izamha.snacky.ui.theme.SnackyTheme
import com.izamha.snacky.viewmodel.SnackViewModel
import com.izamha.snacky.viewmodel.SnackViewModelFactory

@ExperimentalCoilApi
@ExperimentalPermissionsApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
class SignInActivity : ComponentActivity() {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    private val snackViewModel by viewModels<SnackViewModel> {
        SnackViewModelFactory((this.applicationContext as Application))
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("980360320756-3cm0hhl59dbk0u4mpui6fcb3olbrf1oi.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {

            SnackyTheme {
                SignInForm(
                    signIn = { signIn() }
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from  launching the Intent from GoogleSignInApi.getSignInIntent(...)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle: " + account.email)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI accordingly
                Log.w(TAG, "Google Sign in failed: ${e.localizedMessage}", e)
            }

        }

    }

    @ExperimentalPermissionsApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    setContent {
                        SnackyApp(snackViewModel = snackViewModel)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception?.cause)
                    updateUI(null)
                }
            }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        Log.d(TAG, "signIn: Trying to SIGN IN...")
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private var launchHomeActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                snackyToast(this, "This is the MainActivity!")
            }
        }

    @ExperimentalAnimationApi
    private fun openActivity() {
        val homeIntent = Intent(this, MainActivity::class.java)
        launchHomeActivity.launch(homeIntent)
    }

    private fun updateUI(user: FirebaseUser?) {}

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}

@ExperimentalCoilApi
@ExperimentalMaterialApi
@Composable
fun SignInForm(
    modifier: Modifier = Modifier,
    signIn: () -> Unit
) {

    var username by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.placeholder),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
        )

        // Text(text = "Sign Up and get rid of hunger!", fontSize = 18.sp)

        InputItem(
            textFieldValue = username,
            onTextChanged = { username = it },
            label = "Username",
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            trailingIcon = {}
        )

        InputItem(
            textFieldValue = password,
            onTextChanged = { password = it },
            label = "Password",
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            trailingIcon = {}
        )
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .size(24.dp)
        )

        FlexibleButton(
            onClicked = signIn,
            backgroundColor = Color.White,
        )
    }

//    SnackySurface(
//        color = Color.LightGray,
//        shape = CircleShape,
//        elevation = 0.dp,
//        modifier = modifier
//            .padding(vertical = 16.dp)
//            .size(220.dp)
//            .fillMaxSize()
//    ) {
//
//    }

}