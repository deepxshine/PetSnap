package com.example.petsnap.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petsnap.domain.model.LoginResponse
import com.example.petsnap.domain.usecase.LoginUseCase
import com.example.petsnap.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginResult = MutableLiveData<Resource<LoginResponse>>()
    val loginResult: LiveData<Resource<LoginResponse>> get() = _loginResult

    fun login(username: String, password: String) {

        // Начать авторизацию
        viewModelScope.launch {
            _loginResult.value = Resource.loading(null)
            val result = loginUseCase(username, password)
            _loginResult.value = result
        }
    }
}
