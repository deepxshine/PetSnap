package com.example.petsnap.domain.usecase

import com.example.petsnap.domain.model.FollowAndUnfollowRequest
import com.example.petsnap.domain.model.FriendshipResponse
import com.example.petsnap.domain.repository.UserRepository
import com.example.petsnap.utils.Resource
import javax.inject.Inject

class FollowUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke (followRequest: FollowAndUnfollowRequest) : Resource<FriendshipResponse> {
        return userRepository.followUser(followRequest)
    }
}