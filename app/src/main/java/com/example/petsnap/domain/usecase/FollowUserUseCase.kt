package com.example.petsnap.domain.usecase

import com.example.petsnap.domain.repository.UserRepository
import com.example.petsnap.utils.Resource
import javax.inject.Inject

class FollowUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke (followerId: Long, followingId: Long) : Resource<String> {
        return userRepository.followUser(followerId, followingId)
    }
}