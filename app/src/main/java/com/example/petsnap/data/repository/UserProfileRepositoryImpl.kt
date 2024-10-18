package com.example.petsnap.data.repository

import com.example.petsnap.data.remote.UserService
import com.example.petsnap.domain.model.User
import com.example.petsnap.domain.repository.UserProfileRepository
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    private val userService: UserService
) : UserProfileRepository {

    override suspend fun getUserProfile(userId: Long): User {
        return userService.getUserProfile(userId)
    }
}
