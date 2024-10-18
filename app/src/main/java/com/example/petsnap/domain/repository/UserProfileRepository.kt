package com.example.petsnap.domain.repository

import com.example.petsnap.domain.model.User

interface UserProfileRepository {
    suspend fun getUserProfile(userId: Long): User
}
