package com.example.petsnap.data.remote

import com.example.petsnap.domain.model.LikeResponse
import com.example.petsnap.domain.model.PageResponse
import com.example.petsnap.domain.model.PostsOnMainPageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PostService {

    @GET("posts/main/{userId}")
    suspend fun getAllPosts(
        @Path("userId") userId: Long,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 3
    ): PageResponse<PostsOnMainPageResponse>

    @PUT("posts/like/{postId}/{userId}")
    suspend fun addOrRemoveLike(
        @Path("postId") postId: Long,
        @Path("userId") userId: Long
    ): Response<LikeResponse>

    @Multipart
    @POST("posts/createPost/{userId}")
    suspend fun createPost(
        @Part file: MultipartBody.Part,
        @Part("text") text: RequestBody?,
        @Path("userId") userId: Long
    ): Response<PostsOnMainPageResponse>

}