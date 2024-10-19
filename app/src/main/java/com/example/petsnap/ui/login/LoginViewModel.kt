package com.example.petsnap.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petsnap.data.repository.LoginRepositoryImpl
import com.example.petsnap.domain.model.LoginResponse
import com.example.petsnap.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepositoryImpl
) : ViewModel() {

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _loginResult = MutableLiveData<Resource<LoginResponse>>()
    val loginResult: LiveData<Resource<LoginResponse>> get() = _loginResult

    fun login() {
        val user = username.value ?: ""
        val pass = password.value ?: ""

        // Начать авторизацию
        viewModelScope.launch {
            _loginResult.value = Resource.loading(null)
            val result = loginRepository.login(user, pass)
            _loginResult.value = result
        }
    }
}
