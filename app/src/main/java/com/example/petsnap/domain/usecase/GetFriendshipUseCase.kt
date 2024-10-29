package com.example.petsnap.domain.usecase

import com.example.petsnap.domain.model.UserFollowersAndFollowings
import com.example.petsnap.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetFriendshipUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(userId: Long): Flow<UserFollowersAndFollowings> = flow {
        val response = userRepository.getFriendship(userId)
        if (response.isSuccessful) {
            val friendship = response.body() as UserFollowersAndFollowings
            emit(friendship) // отправить данные friendship в Flow
        } else {
            throw Exception("Failed to get followers and followings")
        }
    }.flowOn(Dispatchers.IO)
// flowOn(Dispatchers.IO)：指定 Flow 在 Dispatchers.IO 调度器上运行，以确保网络请求在后台线程中执行。
}