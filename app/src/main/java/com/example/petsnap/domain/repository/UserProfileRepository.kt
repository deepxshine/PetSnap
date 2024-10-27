package com.example.petsnap.domain.repository

import com.example.petsnap.domain.model.UserProfile

interface UserProfileRepository {
    suspend fun getUserProfile(userId: Long): UserProfile
}
