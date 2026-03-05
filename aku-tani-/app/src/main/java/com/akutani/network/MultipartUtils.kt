package com.akutani.network

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URLConnection

private const val TAG = "MultipartUtils"

/**
 * Convert a content Uri into a MultipartBody.Part suitable for Retrofit upload.
 *
 * @param context Context required to access ContentResolver & cache dir.
 * @param uri the Uri selected by user (e.g. from ACTION_GET_CONTENT)
 * @param fieldName the multipart field name expected by backend (e.g. "image" or "avatar")
 *
 * Returns MultipartBody.Part? (null on failure).
 *
 * Usage:
 *    val part = uriToMultipart(context, selectedUri, "image")
 *    api.createArticle(titleRb, excerptRb, contentRb, part)
 */
fun uriToMultipart(context: Context, uri: Uri, fieldName: String): MultipartBody.Part? {
    try {
        val resolver: ContentResolver = context.contentResolver

        // Try to determine filename
        val fileName = queryFilename(resolver, uri) ?: run {
            // fallback to last path segment or random name
            uri.lastPathSegment ?: "upload_${System.currentTimeMillis()}"
        }

        // Determine mime type
        var mimeType = resolver.getType(uri)
        if (mimeType.isNullOrBlank()) {
            // fallback from filename
            mimeType = URLConnection.guessContentTypeFromName(fileName) ?: "application/octet-stream"
        }

        // Create temp file in cache
        val inputStream: InputStream = resolver.openInputStream(uri) ?: return null
        val tempFile = File.createTempFile("upload_", "_" + fileName, context.cacheDir)
        tempFile.deleteOnExit()

        copyStreamToFile(inputStream, tempFile)

        // Build request body & multipart part
        val mediaType: MediaType? = mimeType.toMediaTypeOrNull()
        val reqBody: RequestBody = tempFile.asRequestBody(mediaType)
        return MultipartBody.Part.createFormData(fieldName, fileName, reqBody)
    } catch (e: Exception) {
        Log.e(TAG, "uriToMultipart failed: ${e.message}", e)
        return null
    }
}

/** Helper: copy input stream to file */
private fun copyStreamToFile(input: InputStream, file: File) {
    var out: OutputStream? = null
    try {
        out = FileOutputStream(file)
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var read: Int
        while (input.read(buffer).also { read = it } != -1) {
            out.write(buffer, 0, read)
        }
        out.flush()
    } finally {
        try { input.close() } catch (_: Exception) {}
        try { out?.close() } catch (_: Exception) {}
    }
}

/** Helper: query filename from content resolver (OpenableColumns.DISPLAY_NAME) */
private fun queryFilename(resolver: ContentResolver, uri: Uri): String? {
    var cursor: Cursor? = null
    return try {
        cursor = resolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            val idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (idx != -1) cursor.getString(idx) else null
        } else null
    } catch (e: Exception) {
        Log.w(TAG, "queryFilename failed: ${e.message}")
        null
    } finally {
        cursor?.close()
    }
}
