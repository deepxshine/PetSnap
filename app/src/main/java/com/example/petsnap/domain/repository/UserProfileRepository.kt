package com.example.petsnap.domain.repository

import com.example.petsnap.domain.model.UserProfile

interface UserProfileRepository {
    suspend fun getUserProfile(userId: Long, page: Int = 0, size: Int = 9): UserProfile
}
