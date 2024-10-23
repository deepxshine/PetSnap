package com.example.petsnap.domain.usecase

import com.example.petsnap.domain.model.LoginResponse
import com.example.petsnap.domain.repository.LoginRepository
import com.example.petsnap.utils.Resource
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend operator fun invoke(username: String, password: String) : Resource<LoginResponse> {
        return loginRepository.login(username, password)
    }
}