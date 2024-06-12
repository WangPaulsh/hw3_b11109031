package com.example.hw3_b11109031

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.hw3_b11109031.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class BlurWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        val resourceUri = inputData.getString(MyViewModel.KEY_IMAGE_URI)
        return try {
            val resolver = applicationContext.contentResolver
            val bitmap = uriToBitmap(applicationContext, Uri.parse(resourceUri))
            val blurredBitmap = blurBitmap(bitmap, applicationContext)
            val outputUri = saveBitmapToFile(applicationContext, blurredBitmap)
            val outputData = workDataOf(MyViewModel.KEY_IMAGE_URI to outputUri.toString())
            Result.success(outputData)
        } catch (throwable: Throwable) {
            Result.failure()
        }
    }
}
fun uriToBitmap(context: Context, uri: Uri): Bitmap {
    val inputStream = context.contentResolver.openInputStream(uri)
    return BitmapFactory.decodeStream(inputStream)
}

fun blurBitmap(bitmap: Bitmap, context: Context): Bitmap {
    val outputBitmap = Bitmap.createBitmap(bitmap)
    val renderScript = RenderScript.create(context)
    val input = Allocation.createFromBitmap(renderScript, bitmap)
    val output = Allocation.createFromBitmap(renderScript, outputBitmap)
    val script = ScriptIntrinsicBlur.create(renderScript, input.element)
    script.setRadius(10f)
    script.setInput(input)
    script.forEach(output)
    output.copyTo(outputBitmap)
    return outputBitmap
}

fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri {
    val file = File(context.cacheDir, "blurred_image.png")
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.flush()
    outputStream.close()
    return Uri.fromFile(file)
}