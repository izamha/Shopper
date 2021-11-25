package com.izamha.snacky.ui.payment

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.izamha.snacky.ui.theme.SnackyTheme


@Composable
fun InputItem(
    modifier: Modifier = Modifier,
    textFieldValue: TextFieldValue,
    label: String = "Label",
    onTextChanged: (TextFieldValue) -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: @Composable (() -> Unit)?
) {

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { onTextChanged(it) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next
        ),
        textStyle = textStyle,
        maxLines = 1,
        singleLine = true,
        label = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.body2
                )
            }
        },
        modifier = modifier,
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = SnackyTheme.colors.textPrimary.copy(alpha = ContentAlpha.high),
            unfocusedBorderColor = SnackyTheme.colors.textPrimary.copy(alpha = ContentAlpha.disabled),
            focusedLabelColor = SnackyTheme.colors.textPrimary.copy(alpha = ContentAlpha.high),
            unfocusedLabelColor = SnackyTheme.colors.textPrimary.copy(alpha = ContentAlpha.disabled),
        ),
        trailingIcon = trailingIcon
    )

}

@Preview
@Composable
fun InputItemPreview() {
    InputItemPreview()
}