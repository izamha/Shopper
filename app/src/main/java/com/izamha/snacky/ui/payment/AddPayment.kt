package com.izamha.snacky.ui.payment

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payment
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.statusBarsPadding
import com.izamha.snacky.R
import com.izamha.snacky.ui.components.FlexibleButton
import com.izamha.snacky.ui.components.HorizontalProgress
import com.izamha.snacky.ui.components.SnackyScaffold
import com.izamha.snacky.ui.components.snackyToast
import com.izamha.snacky.ui.theme.Neutral8
import com.izamha.snacky.ui.theme.SnackyTheme
import com.izamha.snacky.ui.utils.mirroringBackIcon

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun AddPayment(
    modifier: Modifier = Modifier,
    upPress: () -> Unit,
    onClickHide: () -> Unit
) {
    var nameText by remember { mutableStateOf(TextFieldValue()) }
    var cardNumber by remember { mutableStateOf(TextFieldValue()) }
    var expiryNumber by remember { mutableStateOf(TextFieldValue()) }
    var cvcNumber by remember { mutableStateOf(TextFieldValue()) }
    var isMoMoPay by remember { mutableStateOf(true) }

    SnackyScaffold {
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .size(56.dp)
        )
        Up(upPress = upPress)
        Column(modifier = Modifier.fillMaxSize()) {

            if (isMoMoPay) {
                PaymentCard(
                    nameText = nameText,
                    cardNumber = cardNumber,
                    expiryNumber = expiryNumber,
                    cvcNumber = cvcNumber
                )
                // Body
                LazyColumn(
                    modifier = modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {

                    item {
                        InputItem(
                            textFieldValue = nameText,
                            label = stringResource(id = R.string.card_holder_name),
                            onTextChanged = { nameText = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            trailingIcon = {}
                        )
                    }

                    item {
                        InputItem(
                            textFieldValue = cardNumber,
                            label = stringResource(id = R.string.card_holder_number),
                            keyboardType = KeyboardType.Number,
                            onTextChanged = { cardNumber = it },
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            visualTransformation = CreditCardFilter,
                            trailingIcon = {}
                        )
                    }

                    item {
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            InputItem(
                                textFieldValue = expiryNumber,
                                label = stringResource(id = R.string.expiry_date),
                                keyboardType = KeyboardType.Number,
                                onTextChanged = { expiryNumber = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                                trailingIcon = {}
                            )
                            InputItem(
                                textFieldValue = cvcNumber,
                                label = stringResource(id = R.string.cvc),
                                keyboardType = KeyboardType.Number,
                                onTextChanged = { cvcNumber = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp),
                                trailingIcon = {}
                            )
                        }
                        // Show some Progress as in you are waiting to cash out
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            // HorizontalProgress()

                            Text(
                                text = "Or use MoMo Pay Instead.",
                                style = TextStyle(color = Color.Gray, fontSize = 18.sp),
                                modifier = Modifier.padding(16.dp)
                            )

                            val context = LocalContext.current

                            OnClickHide {
                                // isMoMoPay = !isMoMoPay
                            }

                        }
                    }
                }
            } else {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(16.dp),
                ) {
                    Text(text = "We are Using MoMo Pay, baby!", fontSize = 32.sp)
                    Button(onClick = { isMoMoPay = !isMoMoPay }) {
                        Text(text = "Show Stuff")
                    }
                }
            }

        }
    }
}

@Composable
private fun Up(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
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

@ExperimentalMaterialApi
@Composable
fun Body(
    modifier: Modifier = Modifier,
    nameText: TextFieldValue,
    expiryNumber: TextFieldValue,
    cardNumber: TextFieldValue,
    cvcNumber: TextFieldValue,

    ) {
    LazyColumn(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {

        item {
            InputItem(
                textFieldValue = nameText,
                label = stringResource(id = R.string.card_holder_name),
                onTextChanged = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                trailingIcon = {}
            )
        }

        item {
            InputItem(
                textFieldValue = cardNumber,
                label = stringResource(id = R.string.card_holder_number),
                keyboardType = KeyboardType.Number,
                onTextChanged = { },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                visualTransformation = CreditCardFilter,
                trailingIcon = {}
            )
        }

        item {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InputItem(
                    textFieldValue = expiryNumber,
                    label = stringResource(id = R.string.expiry_date),
                    keyboardType = KeyboardType.Number,
                    onTextChanged = { },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    trailingIcon = {}
                )
                InputItem(
                    textFieldValue = cvcNumber,
                    label = stringResource(id = R.string.cvc),
                    keyboardType = KeyboardType.Number,
                    onTextChanged = {},
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    trailingIcon = {}
                )
            }
            // Show some Progress as in you are waiting to cash out
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // HorizontalProgress()

                Text(
                    text = "Or use MoMo Pay Instead.",
                    style = TextStyle(color = Color.Gray, fontSize = 18.sp),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun OnClickHide(onClickHide: () -> Unit) {
    val context = LocalContext.current

    FlexibleButton(
        text = "MoMo Pay",
        leadingText = "Kwishyura...",
        icon = painterResource(id = R.drawable.mtn_momo_80x81),
        onClicked = onClickHide,
        backgroundColor = Color.White,
    )
}


@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Preview
@Composable
fun AddPaymentPreview() {
    AddPayment(upPress = {},
        onClickHide = {})
}