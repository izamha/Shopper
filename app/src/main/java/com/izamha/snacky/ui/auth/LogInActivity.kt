package com.izamha.snacky.ui.auth

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.izamha.snacky.R
import com.izamha.snacky.model.User
import com.izamha.snacky.ui.MainActivity
import com.izamha.snacky.ui.components.SnackyButton
import com.izamha.snacky.ui.components.snackyToast
import com.izamha.snacky.ui.theme.SnackyTheme
import com.izamha.snacky.viewmodel.SnackViewModel
import com.izamha.snacky.viewmodel.SnackViewModelFactory
import com.izamha.snacky.viewmodel.UserViewModel
import com.izamha.snacky.viewmodel.UserViewModelFactory

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalPermissionsApi
class LoginActivity : ComponentActivity() {


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        setContent {
            SnackyTheme {
                SignInForm(signIn = {})
            }
        }

    }
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalPermissionsApi
@ExperimentalCoilApi
@ExperimentalMaterialApi
@Composable
fun SignInForm(
    modifier: Modifier = Modifier,
    signIn: () -> Unit
) {

    var username by remember { mutableStateOf(TextFieldValue()) }
    var email by remember { mutableStateOf(TextFieldValue()) }
    var phoneNumber by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isClicked = remember { mutableStateOf(false) }


    val isFormValid by derivedStateOf {
        email.text.isNotBlank() && password.text.length <= 17
    }

    val context = LocalContext.current

    Scaffold(backgroundColor = Color.Gray) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "App Logo",
                modifier = modifier
                    .weight(1f)
                    .size(200.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
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
                        modifier = modifier.padding(vertical = 16.dp),
                        text = "Welcome Back!",
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )

                    Spacer(modifier = modifier.weight(1f))

                    Column(
                        modifier = modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
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

                        if (isClicked.value) {
                            LoginUser(email = email.text)
                        }

                        SnackyButton(
                            enabled = isFormValid,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            onClick = {
                                isClicked.value = true
                            },

                            ) {
                            Text(
                                text = "Log In",
                                fontSize = 24.sp
                            )
                        }

                        Spacer(modifier = modifier.weight(1f))

                        Row(
                            modifier = modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(onClick = {
                                val signUpIntent = Intent(context, SignInActivity::class.java)
                                context.startActivity(signUpIntent)
                            }) {
                                Text(text = "Sign Up")
                            }
                            TextButton(onClick = { /*TODO*/ }) {
                                Text(text = "Forgot Password?", color = Color.Gray)
                            }
                        }

                    }
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@Composable
fun LoginUser(
    email: String,
    password: String = "pspsp"
) {
    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(context.applicationContext as Application)
    )

    val user by userViewModel.getUser(email = email).observeAsState()

    try {
        if (user != null) {
            Log.d("LoginActivity", "LoginUser: $user")
            // After successful User Creation, go to Login
            val loginSuccess = Intent(context, MainActivity::class.java)
            loginSuccess.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(loginSuccess)

        }
    } catch (e: Exception) {
        snackyToast(context, "${e.message}")
    }

}