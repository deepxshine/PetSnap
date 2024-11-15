package com.example.petsnap.ui.home

import androidx.paging.PagingData
import com.example.petsnap.domain.model.CommentsResponse

sealed class CommentsState {
    data object Initial : CommentsState()

    data class Loading(
        val comments: MutableMap<Long, PagingData<CommentsResponse>> = mutableMapOf()
    ) : CommentsState()

    data class Success(val comments: MutableMap<Long, PagingData<CommentsResponse>>) : CommentsState()

    data class Error(val msg: String) : CommentsState()
}