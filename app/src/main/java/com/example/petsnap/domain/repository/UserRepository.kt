package com.example.petsnap.domain.repository

import com.example.petsnap.domain.model.RegisterResponse
import retrofit2.Response
import java.io.File

interface UserRepository {
    suspend fun registerUser(
        username: String,
        password: String,
        birthday: String,
        bio: String?,
        file: File
    ): Response<RegisterResponse>
}