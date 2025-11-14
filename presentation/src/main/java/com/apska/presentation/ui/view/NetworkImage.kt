package com.apska.presentation.ui.view

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter

@Composable
fun NetworkImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    Image(
        painter = rememberAsyncImagePainter(
            model = imageUrl,
            //error = painterResource(R.drawable.ic_error),
            //placeholder = painterResource(R.drawable.ic_placeholder)
        ),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}