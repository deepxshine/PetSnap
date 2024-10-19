package com.example.petsnap.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petsnap.domain.model.User
import com.example.petsnap.domain.repository.RegisterRepository
import com.example.petsnap.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadUserProfile(userId: Long) {
        viewModelScope.launch {
            try {
                // Попытка загрузить профиль пользователя
                val userProfile = userProfileRepository.getUserProfile(userId)
                _user.value = userProfile
            } catch (e: Exception) {
                // Ловим любую ошибку и передаем её в LiveData
                _error.value = handleError(e)
            }
        }
    }

    // Функция для обработки ошибок и возврата сообщения об ошибке
    private fun handleError(e: Exception): String {
        return when (e) {
            is IOException -> "Network error. Please check your connection."
            is HttpException -> {
                when (e.code()) {
                    404 -> "User not found."
                    500 -> "Server error. Please try again later."
                    else -> "Unknown error occurred."
                }
            }
            else -> "An unexpected error occurred."
        }
    }
}


