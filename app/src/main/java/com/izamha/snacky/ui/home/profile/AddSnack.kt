package com.izamha.snacky.ui.home.profile

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.izamha.snacky.R
import com.izamha.snacky.repository.SaveToRoom
import com.izamha.snacky.ui.camera.MainContent
import com.izamha.snacky.ui.components.SnackyButton
import com.izamha.snacky.ui.components.snackyToast
import com.izamha.snacky.ui.payment.CreditCardFilter
import com.izamha.snacky.ui.payment.InputItem
import com.izamha.snacky.ui.theme.Neutral8
import com.izamha.snacky.ui.theme.SnackyTheme
import com.izamha.snacky.ui.utils.mirroringBackIcon


@ExperimentalCoilApi
@ExperimentalPermissionsApi
@Composable
fun AddSnack(
    modifier: Modifier = Modifier,
    upPress: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Up(upPress)
        Body(modifier = modifier, onCameraClick = {})
    }
}

@Composable
private fun Up(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .size(36.dp)
            .background(
                color = Neutral8.copy(alpha = 0.32f),
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = mirroringBackIcon(),
            tint = SnackyTheme.colors.iconInteractive,
            contentDescription = stringResource(R.string.label_back)
        )
    }
}

@ExperimentalCoilApi
@ExperimentalPermissionsApi
@Composable
fun Body(
    modifier: Modifier,
    onCameraClick: () -> Unit
) {
    var snackName by remember { mutableStateOf(TextFieldValue()) }
    var price by remember { mutableStateOf(TextFieldValue()) }
    var imageUrl by remember { mutableStateOf(TextFieldValue()) }
    val context = LocalContext.current
    val showAlert = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(56.dp)
        )

        LazyColumn(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            item {
//                androidx.compose.foundation.Image(
//                    painter = painterResource(id = R.drawable.placeholder),
//                    contentDescription = null
//                )
            }
            item {
                InputItem(
                    textFieldValue = snackName,
                    label = stringResource(id = R.string.snack_name),
                    onTextChanged = { snackName = it },
                    trailingIcon = {
                        Icon(
                            Icons.Filled.Clear,
                            contentDescription = null,
                            modifier = modifier
                                .offset(x = 0.dp)
                                .clickable { snackName = TextFieldValue("") }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }

            item {
                InputItem(
                    textFieldValue = price,
                    label = stringResource(id = R.string.snack_price),
                    keyboardType = KeyboardType.Number,
                    onTextChanged = { price = it },
                    trailingIcon = {
                        Icon(
                            Icons.Filled.Clear,
                            contentDescription = null,
                            modifier = modifier
                                .offset(x = 0.dp)
                                .clickable { price = TextFieldValue("") }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    visualTransformation = CreditCardFilter,
                )
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    InputItem(
                        textFieldValue = imageUrl,
                        label = stringResource(id = R.string.snack_image),
                        keyboardType = KeyboardType.Text,
                        onTextChanged = { imageUrl = it },
                        trailingIcon = {
                            Icon(
                                Icons.Filled.Clear,
                                contentDescription = null,
                                modifier = modifier
                                    .offset(x = 0.dp)
                                    .clickable { imageUrl = TextFieldValue("") }
                            )
                        },
                        modifier = Modifier
                            .padding(vertical = 8.dp),
                    )
                    IconButton(
                        onClick = onCameraClick,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 32.dp)
                            .size(42.dp)
                            .background(
                                color = Neutral8.copy(alpha = 0.32f),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            tint = SnackyTheme.colors.iconInteractive,
                            contentDescription = stringResource(R.string.label_back)
                        )
                    }
                }
            }
        }

        if (showAlert.value) {
            SaveToRoom(
                name = snackName.text,
                imageUrl = imageUrl.text,
                price = price.text.toLong()
            )
        }

        SnackyButton(
            onClick = {
                // Save to Firebase

                // Save to local db, ROOM
                showAlert.value = true

                // Clear the OutlinedTextField
                snackName = TextFieldValue("")
                price = TextFieldValue("")
                imageUrl = TextFieldValue("")
            },
            modifier = modifier.padding(16.dp)
        ) {
            Text(text = "Save to Room")
        }

    }
}

private fun toFirebase(
    name: String,
    imageUrl: String,
    price: String,
    context: Context
) {

    val database = FirebaseDatabase.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val myRef = database.reference.child("Snacks").child(userId!!).push()
    // Save data to the database
    try {
        myRef.child("name").setValue(name)
        myRef.child("imageUrl").setValue(imageUrl)
        myRef.child("price").setValue(price)

        // Sanity check for the user
        snackyToast(context, "Successfully added to Room!")
    } catch (e: Exception) {
        snackyToast(context, "${e.message}")
    }
}


