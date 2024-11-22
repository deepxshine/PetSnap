package com.example.petsnap.data.repository

import com.example.petsnap.data.remote.UserService
import com.example.petsnap.domain.model.FollowAndUnfollowRequest
import com.example.petsnap.domain.model.FriendshipResponse
import com.example.petsnap.domain.model.UserFollowersAndFollowings
import com.example.petsnap.domain.model.UserProfile
import com.example.petsnap.domain.model.UserSearchResponse
import com.example.petsnap.domain.repository.UserRepository
import com.example.petsnap.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService
) : UserRepository {

    override suspend fun getUserProfile(userId: Long, page: Int, size: Int): UserProfile {
        return userService.getUserProfile(userId, page, size)
    }

    override suspend fun getFriendship(userId: Long): Flow<UserFollowersAndFollowings> = flow {
        val response = userService.getFriendship(userId)
        if (response.isSuccessful) {
            val friendship = response.body() as UserFollowersAndFollowings
            emit(friendship) // отправить данные friendship в Flow
        } else {
            throw Exception("Failed to get followers and followings")
        }
    }.flowOn(Dispatchers.IO)
    // flowOn(Dispatchers.IO)：指定 Flow 在 Dispatchers.IO 调度器上运行，以确保网络请求在后台线程中执行。

    override suspend fun followUser(followRequest: FollowAndUnfollowRequest): Resource<FriendshipResponse> {
        return try {
            val response = userService.followUser(followRequest.followerId, followRequest.followingId)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Resource.error(response.message(), null)
            }
        } catch (e: Exception) {
            Resource.error(e.message ?: "Unknown error", null)
        }

    }

    override suspend fun unfollowUser(unfollowRequest: FollowAndUnfollowRequest): Resource<FriendshipResponse> {
        return try {
            val response = userService.unfollowUser(unfollowRequest.followerId, unfollowRequest.followingId)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Resource.error(response.message(), null)
            }
        } catch (e: Exception) {
            Resource.error(e.message ?: "Unknown error", null)
        }
    }

    override suspend fun searchUsers(username: String): List<UserSearchResponse> {
        return userService.searchUsers(username)
    }
}
