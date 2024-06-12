package com.example.hw3_b11109031

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun DetailView(viewModel: MyViewModel, onProcessCompleted: () -> Unit) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.processImage(context)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        viewModel.blurredImageBitmap.value?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
            )
        }
        Button(onClick = onProcessCompleted) {
            Text("Process Completed")
        }
    }
}
