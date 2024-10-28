package com.example.petsnap.domain.model

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


data class CreatePostRequest(
    val file: File,
    val text: String?,
    val userId: Long
) {
    fun textToPart(): RequestBody? {

        val textPart = text?.toRequestBody("text/plain".toMediaTypeOrNull())
        return textPart
    }

    fun toFilePart(): MultipartBody.Part {
        val fileBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", file.name, fileBody)
        return filePart
    }
}
