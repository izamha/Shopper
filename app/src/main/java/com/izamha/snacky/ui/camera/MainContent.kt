package com.izamha.snacky.ui.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.izamha.snacky.ui.utils.EMPTY_IMAGE_URI

@ExperimentalCoilApi
@ExperimentalPermissionsApi
@Composable
fun MainContent(modifier: Modifier = Modifier) {
    var imageUri by remember { mutableStateOf(EMPTY_IMAGE_URI) }

    if (imageUri != EMPTY_IMAGE_URI) {
        Box(modifier = modifier) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberImagePainter(imageUri),
                contentDescription = "Captured image"
            )
            Button(
                modifier = Modifier.align(Alignment.BottomCenter),
                onClick = {
                    imageUri = EMPTY_IMAGE_URI
                }
            ) {
                Text("Remove image")
            }
        }
    } else {
        var showGallerySelect by remember { mutableStateOf(false) }
        if (showGallerySelect) {
            GallerySelect(
                modifier = modifier,
                onImageUri = { uri ->
                    showGallerySelect = false
                    imageUri = uri
                }
            )
        } else {
            Box(modifier = modifier) {
                CameraCapture(
                    modifier = modifier,
                    onImageFile = { file ->
                        imageUri = file.toUri()
                    }
                )
                Button(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(4.dp).background(color = Color.Gray),
                    onClick = {
                        showGallerySelect = true
                    },
                ) {
                    Text("Select from Gallery")
                }
            }
        }
    }

}