package com.izamha.snacky.ui.auth

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.izamha.snacky.R
import com.izamha.snacky.model.Snack
import com.izamha.snacky.model.User
import com.izamha.snacky.repository.SaveToRoom
import com.izamha.snacky.ui.MainActivity
import com.izamha.snacky.ui.SnackyApp
import com.izamha.snacky.ui.components.FlexibleButton
import com.izamha.snacky.ui.components.SnackyButton
import com.izamha.snacky.ui.components.SnackyDivider
import com.izamha.snacky.ui.components.snackyToast
import com.izamha.snacky.ui.theme.SnackyTheme
import com.izamha.snacky.viewmodel.SnackViewModel
import com.izamha.snacky.viewmodel.SnackViewModelFactory
import com.izamha.snacky.viewmodel.UserViewModel
import com.izamha.snacky.viewmodel.UserViewModelFactory

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
                SignUpForm(
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

    private fun createNewAccount(username: String,
                                 email: String,
                                 phoneNumber: String,
                                 password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // verify Email
                    verifyEmail()

                    // Sign in success, update UI with signed-in user's info
                    updateUserInfoAndUI()

                }
            }

    }

    private fun updateUserInfoAndUI() {
        TODO("Not yet implemented")
    }

    private fun verifyEmail() {
        val mUser = auth.currentUser
        mUser!!.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Email verification sent to ${mUser.email}",
                        Toast.LENGTH_LONG).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(
                        this, "Failed to send verification email to " + mUser.email,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

}

@ExperimentalPermissionsApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@ExperimentalMaterialApi
@Composable
fun SignUpForm(
    modifier: Modifier = Modifier,
    signIn: () -> Unit
) {

    var username by remember { mutableStateOf(TextFieldValue()) }
    var email by remember { mutableStateOf(TextFieldValue()) }
    var phoneNumber by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val isClicked = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val isFormValid by derivedStateOf {
        username.text.isNotBlank() && password.text.length <= 17
    }

    Scaffold(backgroundColor = Color.Gray) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
//            Image(
//                painter = painterResource(id = R.drawable.ic_launcher_foreground),
//                contentDescription = "App Logo",
//                modifier = modifier
//                    .weight(1f)
//                    .size(100.dp),
//                colorFilter = ColorFilter.tint(Color.White)
//            )
            Card(
                modifier = modifier
                    .weight(2f)
                    .padding(8.dp),
                shape = RoundedCornerShape(32.dp),
                backgroundColor = SnackyTheme.colors.uiBackground,
            ) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(32.dp)
                        .background(Color.White)
                ) {
                    Text(
                        text = "Create an Account!",
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        modifier = modifier.padding(vertical = 16.dp)
                    )

                    Spacer(modifier = modifier.weight(1f))

                    Column(
                        modifier = modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
                            modifier = modifier.fillMaxWidth(),
                            value = username,
                            onValueChange = { username = it },
                            label = { Text(text = "Username") },
                            singleLine = true,
                            trailingIcon = {
                                if (username.text.isNotBlank()) {
                                    IconButton(onClick = { username = TextFieldValue("") }) {
                                        Icon(
                                            imageVector = Icons.Filled.Clear,
                                            contentDescription = "Clear Icon"
                                        )
                                    }
                                }
                            }
                        )

                        Spacer(modifier = modifier.height(8.dp))

                        OutlinedTextField(
                            modifier = modifier.fillMaxWidth(),
                            value = email,
                            onValueChange = { email = it },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            label = { Text(text = "Email") },
                            singleLine = true,
                            trailingIcon = {
                                if (email.text.isNotBlank()) {
                                    IconButton(onClick = { email = TextFieldValue("") }) {
                                        Icon(
                                            imageVector = Icons.Filled.Clear,
                                            contentDescription = "Clear Icon"
                                        )
                                    }
                                }
                            }
                        )

                        Spacer(modifier = modifier.height(8.dp))


                        OutlinedTextField(
                            modifier = modifier.fillMaxWidth(),
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            label = { Text(text = "Phone Number") },
                            singleLine = true,
                            trailingIcon = {
                                if (phoneNumber.text.isNotBlank()) {
                                    IconButton(onClick = { phoneNumber = TextFieldValue("") }) {
                                        Icon(
                                            imageVector = Icons.Filled.Clear,
                                            contentDescription = "Clear Icon"
                                        )
                                    }
                                }
                            }
                        )


                        Spacer(modifier = modifier.height(12.dp))

                        OutlinedTextField(
                            modifier = modifier.fillMaxWidth(),
                            value = password,
                            onValueChange = { password = it },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            label = { Text(text = "Password") },
                            singleLine = true,
                            trailingIcon = {
                                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                    Icon(
                                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Clear Icon"
                                    )
                                }
                            }
                        )

                        Spacer(modifier = modifier.height(16.dp))

                        // Create a user account
                        SnackyButton(
                            enabled = isFormValid,
                            modifier = modifier.padding(vertical = 16.dp),
                            onClick = {
                                   isClicked.value = true

                                // Create the OutlinedTextField
//                                username = TextFieldValue("")
//                                email = TextFieldValue("")
//                                phoneNumber = TextFieldValue("")
//                                email = TextFieldValue("")

                                // Navigate to LoginActivity
                            },

                        ) {
                            Text(modifier = modifier,
                                text = "Sign Up", fontSize = 24.sp)
                        }

                        if (isClicked.value) {
                            CreateUserAccount(
                                username = username.text,
                                email = email.text,
                                phoneNumber = phoneNumber.text,
                                password = password.text
                            )
                            // snackyToast(context = context, "Attempting to save to DB..")
                        }

                        Spacer(modifier = modifier.weight(1f))

                        FlexibleButton(
                            onClicked = signIn,
                            backgroundColor = Color.White,
                        )

                        Spacer(modifier = modifier.weight(1f))

                        Row(
                            modifier = modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            TextButton(onClick = { /*TODO*/ }) {
                                Text(text = "Already have an account?", color = Color.Gray)
                            }
                            TextButton(onClick = {
                                val signInIntent = Intent(context, LoginActivity::class.java)
                                signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                context.startActivity(signInIntent)
                            }) {
                                Text(text = "Sign In")
                            }
                        }

                    }
                }
            }
        }
    }
}

@ExperimentalCoilApi
@ExperimentalPermissionsApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun CreateUserAccount(
    username: String,
    email: String,
    phoneNumber: String,
    password: String
) {
    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(context.applicationContext as Application)
    )

    val user = User(
        username = username,
        email = email,
        phoneNumber = phoneNumber,
        password = password
    )

    try {
        userViewModel.createUser(user = user)
        snackyToast(context = context, "Successfully created an account!")

        // After successful User Creation, go to Login
        val loginIntent = Intent(context, LoginActivity::class.java)
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(loginIntent)

    } catch (e: Exception) {
        snackyToast(context, "${e.message}")
    }

}



@ExperimentalPermissionsApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@ExperimentalMaterialApi
@Composable
private fun OpenLoginActivity() {
    val context = LocalContext.current
    val signInIntent = Intent(context, LoginActivity::class.java)
    context.startActivity(signInIntent)
}