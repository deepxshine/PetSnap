package com.example.petsnap.domain.usecase

import com.example.petsnap.domain.model.RegisterResponse
import com.example.petsnap.domain.repository.RegisterRepository
import com.example.petsnap.utils.Resource
import java.io.File
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val registerRepository: RegisterRepository) {
    suspend operator fun invoke(username: String, password: String, birthday: String?, bio: String?, file: File?): Resource<RegisterResponse> {
        return registerRepository.registerUser(username, password, birthday, bio, file)
    }
}