package com.example.petsnap.data.remote

import com.example.petsnap.domain.model.CommentsResponse
import com.example.petsnap.domain.model.PageResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentService {

    @GET("/comments/{postId}/{userId}")
    suspend fun getPostComments(
        @Path("postId") postId: Long,
        @Path("userId") userId: Long,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): PageResponse<CommentsResponse>
}