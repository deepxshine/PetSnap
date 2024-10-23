package com.example.petsnap.domain.repository

import androidx.paging.PagingSource
import com.example.petsnap.domain.model.PostsOnMainPageResponse

interface PostRepository {

    fun getPagingSource(userId: Long): PagingSource<Int, PostsOnMainPageResponse>

}