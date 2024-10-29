package com.example.petsnap.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petsnap.domain.model.PostOnProfile
import com.example.petsnap.domain.model.UserProfile
import com.example.petsnap.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile> = _userProfile

    private val _posts = MutableLiveData<List<PostOnProfile>>()
    val posts: LiveData<List<PostOnProfile>> = _posts

    private var currentPage = 0
    val pageSize = 9
    var isLoading = false
    var isLastPage = false

    var userId: Long = -1L

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadUserProfile(userId: Long) {
        this.userId = userId
        currentPage = 0 // Сбрасываем текущую страницу
        isLastPage = false // Сбрасываем флаг последней страницы
        viewModelScope.launch {
            try {
                val userProfile = userRepository.getUserProfile(userId, page = currentPage, size = pageSize)
                _userProfile.value = userProfile
                _posts.value = userProfile.posts // Устанавливаем начальные посты
            } catch (e: Exception) {
                _error.value = handleError(e)
            }
        }
    }

    fun loadMorePosts() {
        if (isLoading || isLastPage) return // Условие, если загрузка идет или последняя страница

        isLoading = true
        viewModelScope.launch {
            try {
                currentPage++
                val additionalProfileData = userRepository.getUserProfile(userId, page = currentPage, size = pageSize)

                val currentPosts = _posts.value.orEmpty()
                _posts.value = currentPosts + additionalProfileData.posts

                // Проверяем, есть ли посты, которые мы загрузили
                if (additionalProfileData.posts.isEmpty()) {
                    isLastPage = true // Устанавливаем флаг, если нет постов
                } else if (additionalProfileData.posts.size < pageSize) {
                    isLastPage = true // Если загруженные посты меньше размера страницы, устанавливаем флаг
                }
            } catch (e: Exception) {
                _error.value = handleError(e)
            } finally {
                isLoading = false // Сбрасываем состояние загрузки
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


