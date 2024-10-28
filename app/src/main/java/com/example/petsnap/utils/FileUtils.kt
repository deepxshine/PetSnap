package com.example.petsnap.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.widget.Toast
import java.io.File

object FileUtils {
    fun createTempFileFromUri(contentResolver: ContentResolver, uri: Uri): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val tempFile = File.createTempFile("temp_image", ".jpg")
            inputStream?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun fileSizeLimit(context: Context, file: File) {
        if (file.length() > 10 * 1024 * 1024) { // если фото больше 10mb
            Toast.makeText(
                context,
                "File size exceeds the limit (10MB)",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}