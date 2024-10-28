package com.example.petsnap.ui.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petsnap.domain.model.CreatePostRequest
import com.example.petsnap.domain.model.PostsOnMainPageResponse
import com.example.petsnap.domain.usecase.CreatePostUseCase
import com.example.petsnap.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(private val createPostUseCase: CreatePostUseCase) : ViewModel() {

    private val _newPost = MutableLiveData<Resource<PostsOnMainPageResponse>>()
    val newPost: LiveData<Resource<PostsOnMainPageResponse>> = _newPost

    fun createPost(file: File,
                   text: String?,
                   userId: Long) {
        viewModelScope.launch {
            _newPost.value = Resource.loading(null)
            try {
                val newPost = CreatePostRequest(file, text, userId)
                val response = createPostUseCase(newPost)

                if (response.isSuccessful) {
                    _newPost.value = Resource.success(response.body())
                } else {
                    _newPost.value = Resource.error(response.message(), null)
                }
            } catch (e: Exception) {
                _newPost.value = Resource.error(e.message ?: "Unknown error", null)
            }

        }
    }

}