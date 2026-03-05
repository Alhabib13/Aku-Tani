package com.akutani.utils

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

/**
 * Convert Uri -> MultipartBody.Part (AMAN untuk Retrofit + Laravel)
 */
fun uriToMultipart(
    context: Context,
    uri: Uri,
    paramName: String
): MultipartBody.Part? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null

        // file sementara (AMAN, tidak tergantung metadata)
        val file = File(
            context.cacheDir,
            "upload_${System.currentTimeMillis()}.jpg"
        )

        FileOutputStream(file).use { output ->
            inputStream.copyTo(output)
        }

        val requestBody = file.readBytes()
            .toRequestBody("image/*".toMediaTypeOrNull())

        MultipartBody.Part.createFormData(
            paramName,
            file.name,
            requestBody
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
