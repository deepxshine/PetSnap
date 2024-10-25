package com.example.petsnap.ui.home

import androidx.paging.PagingData
import com.example.petsnap.domain.model.LikeResponse
import com.example.petsnap.domain.model.PostsOnMainPageResponse
import com.example.petsnap.utils.Resource

sealed class HomeScreenState {

    data object Initial : HomeScreenState()

    //    data object Loading: HomeScreenState()
    data class Loading(val posts: PagingData<PostsOnMainPageResponse> = PagingData.empty()) :
        HomeScreenState()

    data class Success( // параметры для показа экрана в успешном состоянии
        val posts: PagingData<PostsOnMainPageResponse>, // posts state
        val likeResult: Resource<LikeResponse>? = null // likeResult
    ) : HomeScreenState()

    data class Error(val msg: String) : HomeScreenState()
}