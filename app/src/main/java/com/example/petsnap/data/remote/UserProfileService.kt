package com.example.petsnap.data.remote

import com.example.petsnap.domain.model.UserProfile
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserProfileService {
    @GET("/user/userPage/{userId}")
    suspend fun getUserProfile(
        @Path("userId") userId: Long,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 9
    ): UserProfile
}
