package com.example.petsnap.ui.register

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petsnap.domain.model.RegisterResponse
import com.example.petsnap.domain.repository.UserRepository
import com.example.petsnap.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _registerResult = MutableLiveData<Resource<RegisterResponse>>()
    val registerResult: LiveData<Resource<RegisterResponse>> get() = _registerResult

    fun registerUser(username: String, password: String, birthday: String, bio: String?, file: File) {
        viewModelScope.launch {
            _registerResult.value = Resource.loading(null)
            try {
                val response = userRepository.registerUser(username, password, birthday, bio, file)
                if (response.isSuccessful) {
                    _registerResult.value = Resource.success(response.body()!!)
                } else {
                    _registerResult.value = Resource.error(response.message(), null)
                }
            } catch (e: Exception) {
                _registerResult.value = Resource.error(e.message ?: "Unknown error", null)
            }
        }
    }
}

