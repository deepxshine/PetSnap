package com.example.petsnap.domain.usecase

import com.example.petsnap.domain.model.UserSearchResponse
import com.example.petsnap.domain.repository.UserRepository
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(username: String): List<UserSearchResponse> {
        return userRepository.searchUsers(username)
    }
}