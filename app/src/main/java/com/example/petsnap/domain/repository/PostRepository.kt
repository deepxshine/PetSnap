package com.example.petsnap.domain.repository

import androidx.paging.PagingSource
import com.example.petsnap.domain.model.CreatePostRequest
import com.example.petsnap.domain.model.PostsOnMainPageResponse
import com.example.petsnap.utils.Resource

interface PostRepository {

    fun getPagingSource(userId: Long): PagingSource<Int, PostsOnMainPageResponse>

    suspend fun createPost(createPostRequest: CreatePostRequest): Resource<PostsOnMainPageResponse>

}