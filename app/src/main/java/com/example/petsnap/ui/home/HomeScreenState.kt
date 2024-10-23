package com.example.petsnap.ui.home

import androidx.paging.PagingData
import com.example.petsnap.domain.model.PostsOnMainPageResponse

sealed class HomeScreenState {
    data object Initial: HomeScreenState()

    data object Loading: HomeScreenState()

    data class Success(
        val posts: PagingData<PostsOnMainPageResponse>
    ): HomeScreenState()

    data class Error(val msg: String): HomeScreenState()
}