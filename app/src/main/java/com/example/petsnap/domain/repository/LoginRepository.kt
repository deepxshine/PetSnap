package com.example.petsnap.domain.repository

import com.example.petsnap.domain.model.LoginResponse
import com.example.petsnap.utils.Resource

interface LoginRepository {
    suspend fun login(username: String, password: String): Resource<LoginResponse>
}