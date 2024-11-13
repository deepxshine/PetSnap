package com.example.petsnap.domain.usecase

import com.example.petsnap.domain.model.UserProfile
import com.example.petsnap.domain.repository.UserRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: Long, page: Int, size: Int): UserProfile {
        return userRepository.getUserProfile(userId, page, size)
    }
}
