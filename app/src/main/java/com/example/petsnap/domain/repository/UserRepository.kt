package com.example.petsnap.domain.repository

import com.example.petsnap.domain.model.UserFollowersAndFollowings
import com.example.petsnap.domain.model.UserProfile
import com.example.petsnap.utils.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface UserRepository {
    suspend fun getUserProfile(userId: Long, page: Int = 0, size: Int = 9): UserProfile

    suspend fun getFriendship(userId: Long): Flow<UserFollowersAndFollowings>

    suspend fun followUser(followerId: Long, followingId: Long): Resource<String>

    suspend fun unfollowUser(followerId: Long, followingId: Long): Resource<String>

}
