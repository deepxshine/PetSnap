package com.example.petsnap.domain.usecase

import com.example.petsnap.domain.model.UserFollowersAndFollowings
import com.example.petsnap.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFriendshipUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: Long): Flow<UserFollowersAndFollowings> {
        return userRepository.getFriendship(userId)
    }
}