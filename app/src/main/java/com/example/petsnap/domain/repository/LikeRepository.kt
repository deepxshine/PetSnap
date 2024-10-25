package com.example.petsnap.domain.repository

import com.example.petsnap.domain.model.LikeRequest
import com.example.petsnap.domain.model.LikeResponse
import com.example.petsnap.utils.Resource

interface LikeRepository {

    suspend fun addOrRemoveLike(likeRequest: LikeRequest) : Resource<LikeResponse>
}