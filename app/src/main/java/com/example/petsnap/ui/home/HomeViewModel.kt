package com.example.petsnap.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.petsnap.domain.model.LikeRequest
import com.example.petsnap.domain.model.PostsOnMainPageResponse
import com.example.petsnap.domain.usecase.AddOrRemoveLikeUseCase
import com.example.petsnap.domain.usecase.GetAllPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllPostsUseCase: GetAllPostsUseCase,
    private val addOrRemoveLikeUseCase: AddOrRemoveLikeUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeScreenState> =
        MutableStateFlow(HomeScreenState.Initial)
    val uiState: StateFlow<HomeScreenState> = _uiState.asStateFlow()

    private fun getPosts(userId: Long): Flow<PagingData<PostsOnMainPageResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 3,
                initialLoadSize = 3,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { getAllPostsUseCase.getPagingSource(userId) }
        ).flow.cachedIn(viewModelScope)
    }


    fun loadPosts(userId: Long) {
        viewModelScope.launch {
            _uiState.value = HomeScreenState.Loading(
                posts = PagingData.empty()
            )
            try {
                getPosts(userId).collectLatest { pagingData ->

                    _uiState.value = HomeScreenState.Success(
                        posts = pagingData,
                        likeResult = null // изначальный результат лайка - null
                    )

                }
            } catch (e: Exception) {
                _uiState.value = HomeScreenState.Error(e.message ?: "Unknown error")
            }

        }
    }
    fun addOrRemoveLike(postId: Long, userId: Long) {
        viewModelScope.launch {
            val likeRequest = LikeRequest(postId, userId)
            val response = addOrRemoveLikeUseCase(likeRequest)
            val currentState = _uiState.value

            if (currentState is HomeScreenState.Success) {
                // 在 PagingData 上应用 map 转换
                val updatedPosts = currentState.posts.map { post ->
                    Log.d("ViewModel", "Processing post: $post")
                    if (post.id == postId) {
                        response.data?.let {
                            post.copy(
                                likesCount = it.likesCount,
                                likedByUser = !post.likedByUser
                            )
                        } ?: post
                    } else {
                        post
                    }
                }

                // обновлять uiState
                _uiState.value = HomeScreenState.Success(
                    posts = updatedPosts,
                    likeResult = response
                )
            }
        }
    }


}