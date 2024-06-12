package com.example.hw3_b11109031

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
//import com.example.hw3_b11109031.ImageProcessingWorker
import androidx.work.*
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {
    val imageUri = mutableStateOf<Uri?>(null)
    val blurredImageBitmap = mutableStateOf<Bitmap?>(null)

    fun setImageUri(uri: Uri) {
        imageUri.value = uri
    }

    fun processImage(context: Context) {
        imageUri.value?.let { uri ->
            viewModelScope.launch {
                val bitmap = uriToBitmap(context, uri)
                val blurredBitmap = blurBitmap(bitmap, context)
                blurredImageBitmap.value = blurredBitmap
            }
        }
    }


    fun enqueueImageProcessingWork(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val blurRequest = OneTimeWorkRequestBuilder<BlurWorker>()
            .setInputData(createInputDataForUri())
            .build()

        workManager.enqueue(blurRequest)
    }

    private fun createInputDataForUri(): Data {
        val builder = Data.Builder()
        imageUri.value?.let {
            builder.putString(KEY_IMAGE_URI, it.toString())
        }
        return builder.build()
    }

    companion object {
        const val KEY_IMAGE_URI = "IMAGE_URI"
    }
}