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
import com.example.hw3_b11109031.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class ImageProcessingWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val imageUri = inputData.getString("imageUri") ?: return@withContext Result.failure()
        val context = applicationContext
        val uri = Uri.parse(imageUri)

        val inputStream = context.contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        val bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)

        val renderScript = RenderScript.create(context)
        val input = Allocation.createFromBitmap(renderScript, bitmap)
        val output = Allocation.createTyped(renderScript, input.type)
        val script = ScriptIntrinsicBlur.create(renderScript, input.element)
        script.setRadius(10f)
        script.setInput(input)
        script.forEach(output)
        output.copyTo(bitmap)

        renderScript.destroy()

        // 在这里可以保存模糊处理后的图像或其他处理
        return@withContext Result.success()
    }
}