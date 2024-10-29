package com.example.petsnap.domain.repository

import com.example.petsnap.domain.model.UserFollowersAndFollowings
import com.example.petsnap.domain.model.UserProfile
import retrofit2.Response

interface UserRepository {
    suspend fun getUserProfile(userId: Long, page: Int = 0, size: Int = 9): UserProfile

    suspend fun getFriendship(userId: Long): Response<UserFollowersAndFollowings>

}
