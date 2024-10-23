package com.example.petsnap.domain.usecase

import androidx.paging.PagingSource
import com.example.petsnap.domain.model.PostsOnMainPageResponse
import com.example.petsnap.domain.repository.PostRepository
import javax.inject.Inject

class GetAllPostsUseCase @Inject constructor(private val postRepository: PostRepository) {

    fun getPagingSource(userId: Long): PagingSource<Int, PostsOnMainPageResponse> {
        return postRepository.getPagingSource(userId)
    }
}