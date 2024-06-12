package com.example.hw3_b11109031

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.hw3_b11109031.ImageProcessingWorker
import androidx.work.*
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {
    private val _imageUri = mutableStateOf<Uri?>(null)
    val imageUri: State<Uri?> = _imageUri

    private val _blurredImageBitmap = mutableStateOf<Bitmap?>(null)
    val blurredImageBitmap: State<Bitmap?> = _blurredImageBitmap

    fun setImageUri(uri: Uri) {
        _imageUri.value = uri
    }

    fun processImage(context: Context) {
        viewModelScope.launch {
            _imageUri.value?.let { uri ->
                val blurredBitmap = blurImage(context, uri)
                _blurredImageBitmap.value = blurredBitmap
            }
        }
    }

    fun enqueueImageProcessingWork(context: Context) {
        _imageUri.value?.let { uri ->
            val inputData = Data.Builder()
                .putString("imageUri", uri.toString())
                .build()

            val workRequest = OneTimeWorkRequestBuilder<ImageProcessingWorker>()
                .setInputData(inputData)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}