package com.example.petsnap.domain.usecase

import com.example.petsnap.domain.model.CreatePostRequest
import com.example.petsnap.domain.model.PostsOnMainPageResponse
import com.example.petsnap.domain.repository.PostRepository
import retrofit2.Response
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(private val postRepository: PostRepository) {

    suspend operator fun invoke(createPostRequest: CreatePostRequest) : Response<PostsOnMainPageResponse> {
        return postRepository.createPost(createPostRequest)
    }
}