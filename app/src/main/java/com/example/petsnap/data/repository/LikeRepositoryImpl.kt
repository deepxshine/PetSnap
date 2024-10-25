package com.example.petsnap.data.repository

import com.example.petsnap.data.remote.PostService
import com.example.petsnap.domain.model.LikeRequest
import com.example.petsnap.domain.model.LikeResponse
import com.example.petsnap.domain.repository.LikeRepository
import com.example.petsnap.utils.Resource
import javax.inject.Inject


class LikeRepositoryImpl @Inject constructor(private val postService: PostService) :
    LikeRepository {

    override suspend fun addOrRemoveLike(likeRequest: LikeRequest): Resource<LikeResponse> {

        return try {
            val response = postService.addOrRemoveLike(likeRequest.postId, likeRequest.userId)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Resource.error("like failed", null)
            }
        } catch (e: Exception) {
            Resource.error("Network error", null)
        }
    }
}

