package com.example.hw3_b11109031

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

@Composable
fun TakePhotoScreen(onPhotoTaken: (Uri) -> Unit) {
    val context = LocalContext.current
    val photoFile = remember {
        File.createTempFile("photo_", ".jpg", context.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
    }
    val photoUri = remember { FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", photoFile) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            onPhotoTaken(photoUri)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = { launcher.launch(photoUri) }) {
            Text("Take a Photo")
        }
    }
}

