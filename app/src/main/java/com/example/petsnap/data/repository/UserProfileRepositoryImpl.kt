package com.example.petsnap.data.repository

import com.example.petsnap.data.remote.UserProfileService
import com.example.petsnap.domain.model.UserProfile
import com.example.petsnap.domain.repository.UserProfileRepository
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    private val userProfileService: UserProfileService
) : UserProfileRepository {

    override suspend fun getUserProfile(userId: Long, page: Int, size: Int): UserProfile {
        return userProfileService.getUserProfile(userId, page, size)
    }
}
