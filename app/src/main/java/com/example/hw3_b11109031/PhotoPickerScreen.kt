package com.example.hw3_b11109031

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PhotoPickerScreen(onPhotoSelected: (Uri) -> Unit) {
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let { onPhotoSelected(it) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Pick a Photo")
        }
    }
}