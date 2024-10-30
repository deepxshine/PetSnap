package com.example.petsnap.domain.usecase

import com.example.petsnap.domain.model.UserFollowersAndFollowings
import com.example.petsnap.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetFriendshipUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: Long): Flow<UserFollowersAndFollowings> {
        return userRepository.getFriendship(userId)
    }
}