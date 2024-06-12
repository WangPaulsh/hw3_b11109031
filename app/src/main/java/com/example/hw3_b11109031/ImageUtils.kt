package com.example.hw3_b11109031

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

suspend fun blurImage(context: Context, uri: Uri): Bitmap? = withContext(Dispatchers.IO) {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val originalBitmap = BitmapFactory.decodeStream(inputStream)

    val bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)

    val renderScript = RenderScript.create(context)
    val input = Allocation.createFromBitmap(renderScript, bitmap)
    val output = Allocation.createTyped(renderScript, input.type)
    val script = ScriptIntrinsicBlur.create(renderScript, input.element)
    script.setRadius(10f)  // Adjust blur radius as needed
    script.setInput(input)
    script.forEach(output)
    output.copyTo(bitmap)

    renderScript.destroy()
    return@withContext bitmap
}