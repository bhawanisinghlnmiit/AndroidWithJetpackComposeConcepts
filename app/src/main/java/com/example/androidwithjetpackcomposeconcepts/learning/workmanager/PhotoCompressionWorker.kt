package com.example.androidwithjetpackcomposeconcepts.learning.workmanager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.roundToInt

// Coroutine worker which runs in the suspend functions
// if we want to send some data to the worker, then we have to pass that to worker parameters
// this class we won't be instantiating , android OS will be doing that
class PhotoCompressionWorker(
    private val applicationContext : Context,
    private val parameters: WorkerParameters
): CoroutineWorker(
    applicationContext,
    parameters
) {
    // this is the function which will be executed when we want to run our worker
    // also we can run periodically
    // also under some constraints
    // it returns a result
    // we have to tell this worker in what scenario this work is considered success, failure or retry it at later point
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO){
            val stringUri = parameters.inputData.getString(KEY_CONTENT_URI)
            val compressionThresholdInBytes = parameters.inputData.getLong(
                KEY_COMPRESSION_THRESHOLD,
                0L
            )
            val uri = Uri.parse(stringUri) // to construct the real uri out
            val bytes = applicationContext.contentResolver.openInputStream(uri)?.use {
                it.readBytes()
            } ?: return@withContext Result.failure()

            // now we have bytearray , how do we interpret that as an image and compress that image,
            // for that we need to convert it to BitMap, its image object that we use in Android

            val bitMap = BitmapFactory.decodeByteArray(bytes, 0 , bytes.size)

            var quality = 100
            var outPutBytes: ByteArray

            do {
                val outputStream = ByteArrayOutputStream()
                outputStream.use {outputStream ->
                    bitMap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                }
                outPutBytes = outputStream.toByteArray()
                quality -= (quality * 0.1).roundToInt()

            } while (outPutBytes.size>compressionThresholdInBytes  && quality> 5)

            val file = File(applicationContext.cacheDir, "${parameters.id}.jpg")
            file.writeBytes(outPutBytes)
            // Now our file is saved into our file system

            return@withContext Result.success(
                workDataOf(
                    KEY_RESULT_PATH to file.absolutePath
                )
            )
        }
    }

    companion object {
        const val KEY_CONTENT_URI = "KEY_CONTENT_URI"
        const val KEY_COMPRESSION_THRESHOLD = "KEY_COMPRESSION_THRESHOLD"
        const val KEY_RESULT_PATH = "KEY_RESULT_PATH"
    }
}