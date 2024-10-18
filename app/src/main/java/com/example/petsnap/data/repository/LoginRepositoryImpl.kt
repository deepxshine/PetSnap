package com.example.petsnap.data.repository

import com.example.petsnap.data.remote.LoginService
import com.example.petsnap.domain.model.LoginRequest
import com.example.petsnap.domain.model.LoginResponse
import com.example.petsnap.domain.repository.LoginRepository
import com.example.petsnap.utils.Resource
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginService: LoginService
): LoginRepository {

    override suspend fun login(username: String, password: String): Resource<LoginResponse> {
        return try {
            val response = loginService.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Resource.error("Login failed", null)
            }
        } catch (e: Exception) {
            Resource.error("Network error", null)
        }
    }
}