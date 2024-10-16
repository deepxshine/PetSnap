package com.example.petsnap.data.remote

import com.example.petsnap.domain.model.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RegisterService {

    @Multipart
    @POST("/user/register")
    suspend fun registerUser(
        @Part("username") username: RequestBody,
        @Part("password") password: RequestBody,
        @Part("birthday") birthday: RequestBody,
        @Part("bio") bio: RequestBody?,
        @Part file: MultipartBody.Part
    ): Response<RegisterResponse>

}