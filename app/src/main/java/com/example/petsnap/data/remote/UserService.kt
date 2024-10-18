package com.example.petsnap.data.remote

import com.example.petsnap.domain.model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("/user/userPage/{userId}")
    suspend fun getUserProfile(@Path("userId") userId: Long): User
}
