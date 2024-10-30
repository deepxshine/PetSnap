package com.example.petsnap.domain.repository

import com.example.petsnap.domain.model.FollowAndUnfollowRequest
import com.example.petsnap.domain.model.FriendshipResponse
import com.example.petsnap.domain.model.UserFollowersAndFollowings
import com.example.petsnap.domain.model.UserProfile
import com.example.petsnap.utils.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserProfile(userId: Long, page: Int = 0, size: Int = 9): UserProfile

    suspend fun getFriendship(userId: Long): Flow<UserFollowersAndFollowings>

    suspend fun followUser(followRequest: FollowAndUnfollowRequest): Resource<FriendshipResponse>

    suspend fun unfollowUser(unfollowRequest: FollowAndUnfollowRequest): Resource<FriendshipResponse>

}
