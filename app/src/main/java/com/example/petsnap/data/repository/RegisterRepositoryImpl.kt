package com.example.petsnap.data.repository


import com.example.petsnap.data.remote.RegisterService
import com.example.petsnap.domain.model.RegisterRequest
import com.example.petsnap.domain.model.RegisterResponse
import com.example.petsnap.domain.repository.RegisterRepository

import retrofit2.Response
import java.io.File
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(private val registerService: RegisterService) : RegisterRepository {

    override suspend fun registerUser(
        username: String,
        password: String,
        birthday: String?,
        bio: String?,
        file: File?
    ): Response<RegisterResponse> {
        val registerRequest = RegisterRequest(username, password, birthday, bio, file)

        val parts = registerRequest.toRequestParts()
        val filePart = registerRequest.toFilePart()

        val response = registerService.registerUser(
            parts.find { it.first == "username" }?.second ?: throw IllegalArgumentException("Username is missing"),
            parts.find { it.first == "password" }?.second ?: throw IllegalArgumentException("Password is missing"),
            parts.find { it.first == "birthday" }?.second,
            parts.find { it.first == "bio" }?.second,
            filePart
        )
        return response
    }
}



