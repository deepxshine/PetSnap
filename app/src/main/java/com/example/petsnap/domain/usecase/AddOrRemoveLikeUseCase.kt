package com.example.petsnap.domain.usecase

import com.example.petsnap.domain.model.LikeRequest
import com.example.petsnap.domain.model.LikeResponse
import com.example.petsnap.domain.repository.LikeRepository
import com.example.petsnap.utils.Resource
import javax.inject.Inject

class AddOrRemoveLikeUseCase @Inject constructor(private val likeRepository: LikeRepository) {

    suspend operator fun invoke (likeRequest: LikeRequest) : Resource<LikeResponse> {
        return likeRepository.addOrRemoveLike(likeRequest)
    }
}