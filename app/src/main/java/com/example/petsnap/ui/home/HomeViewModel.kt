package com.example.petsnap.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.petsnap.domain.model.PostsOnMainPageResponse
import com.example.petsnap.domain.usecase.GetAllPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllPostsUseCase: GetAllPostsUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeScreenState> = MutableStateFlow(HomeScreenState.Initial)
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
            _uiState.value = HomeScreenState.Loading
            try {
                getPosts(userId).collect { pagingData ->
                    _uiState.value = HomeScreenState.Success(pagingData)
                }
            } catch (e: Exception) {
                _uiState.value = HomeScreenState.Error(e.message ?: "Unknown error")
            }
        }
    }
}