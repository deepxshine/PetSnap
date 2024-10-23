package com.example.petsnap.domain.model

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class RegisterRequest(
    val username: String,
    val password: String,
    val birthday: String?,
    val bio: String?,
    val file: File?
) {
    fun toRequestParts(): List<Pair<String, RequestBody>> {
        val parts = mutableListOf<Pair<String, RequestBody>>()

        parts.add("username" to username.toRequestBody("text/plain".toMediaTypeOrNull()))
        parts.add("password" to password.toRequestBody("text/plain".toMediaTypeOrNull()))
        birthday?.let {
            parts.add("birthday" to it.toRequestBody("text/plain".toMediaTypeOrNull()))
        }
        bio?.let {
            parts.add("bio" to it.toRequestBody("text/plain".toMediaTypeOrNull()))
        }

        return parts
    }

    fun toFilePart(): MultipartBody.Part? {
        return file?.let {
            val fileBody = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("file", file.name, fileBody)
        }
    }
}

