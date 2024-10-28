package com.example.petsnap.domain.repository

import androidx.paging.PagingSource
import com.example.petsnap.domain.model.CreatePostRequest
import com.example.petsnap.domain.model.PostsOnMainPageResponse
import retrofit2.Response

interface PostRepository {

    fun getPagingSource(userId: Long): PagingSource<Int, PostsOnMainPageResponse>

    suspend fun createPost(createPostRequest: CreatePostRequest): Response<PostsOnMainPageResponse>

}