package com.example.petsnap.data.repository


import com.example.petsnap.data.remote.RegisterService
import com.example.petsnap.domain.model.RegisterResponse
import com.example.petsnap.domain.repository.UserRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MultipartBody

import retrofit2.Response
import java.io.File
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(private val registerService: RegisterService) :
    UserRepository {

    override suspend fun registerUser(
        username: String,
        password: String,
        birthday: String,
        bio: String?,
        file: File
    ) : Response<RegisterResponse> {
        val usernameBody = username.toRequestBody("text/plain".toMediaTypeOrNull())
        val passwordBody = password.toRequestBody("text/plain".toMediaTypeOrNull())
        val birthdayBody = birthday.toRequestBody("text/plain".toMediaTypeOrNull())
        val bioBody = bio?.toRequestBody("text/plain".toMediaTypeOrNull())

        val fileBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", file.name, fileBody)

        val response = registerService.registerUser(usernameBody, passwordBody, birthdayBody, bioBody, filePart)
        return response
    }
}


