package com.example.petsnap.data.remote

import com.example.petsnap.domain.model.LoginRequest
import com.example.petsnap.domain.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/user/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}