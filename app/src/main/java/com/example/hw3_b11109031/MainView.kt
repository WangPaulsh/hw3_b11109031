package com.example.hw3_b11109031

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.hw3_b11109031.MyViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.io.File
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainView(onPhotoPickerClick: () -> Unit, onTakePhotoClick: () -> Unit, viewModel: MyViewModel) {
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions = listOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.CAMERA
    ))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (multiplePermissionsState.allPermissionsGranted) {
            Text("All permissions granted", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onTakePhotoClick) {
                Text("Take a Photo")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onPhotoPickerClick) {
                Text("Choose from Gallery")
            }
        } else {
            Column {
                Text("Permissions needed", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { multiplePermissionsState.launchMultiplePermissionRequest() }) {
                    Text("Request Permissions")
                }
            }
        }
    }
}