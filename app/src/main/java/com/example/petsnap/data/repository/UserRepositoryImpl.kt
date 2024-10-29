package com.example.petsnap.data.repository

import com.example.petsnap.data.remote.UserService
import com.example.petsnap.domain.model.UserFollowersAndFollowings
import com.example.petsnap.domain.model.UserProfile
import com.example.petsnap.domain.repository.UserRepository
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService
) : UserRepository {

    override suspend fun getUserProfile(userId: Long, page: Int, size: Int): UserProfile {
        return userService.getUserProfile(userId, page, size)
    }

    override suspend fun getFriendship(userId: Long): Response<UserFollowersAndFollowings> {
        return userService.getFriendship(userId)
    }
}
