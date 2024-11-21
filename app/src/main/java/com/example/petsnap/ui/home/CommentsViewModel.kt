package com.example.petsnap.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.petsnap.domain.model.CommentsRequest
import com.example.petsnap.domain.model.CommentsResponse
import com.example.petsnap.domain.usecase.AddCommentUseCase
import com.example.petsnap.domain.usecase.GetPostCommentsUseCase
import com.example.petsnap.domain.usecase.RemoveCommentUseCase
import com.example.petsnap.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val getPostCommentsUseCase: GetPostCommentsUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val removeCommentUseCase: RemoveCommentUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<CommentsState> =
        MutableStateFlow(CommentsState.Initial)
    val uiState: StateFlow<CommentsState> = _uiState.asStateFlow()

    private suspend fun getCommentsData(
        commentsRequest: CommentsRequest
    ): Flow<PagingData<CommentsResponse>> {
        val comments = getPostCommentsUseCase(commentsRequest)

        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { comments }
        ).flow.cachedIn(viewModelScope)
    }

    private suspend fun getComments(postId: Long, userId: Long) {
        try {
            val commentsRequest = CommentsRequest(postId, userId)
            getCommentsData(commentsRequest).collectLatest { comments ->
                _uiState.value = CommentsState.Success(
                    comments = mutableMapOf(postId to comments)
                )
            }

        } catch (e: Exception) {
            _uiState.value = CommentsState.Error(e.message ?: "Unknown error")
        }
    }

    fun loadComments(postId: Long, userId: Long) {
        viewModelScope.launch {

            _uiState.value = CommentsState.Loading(
                comments = mutableMapOf(),
            )
            getComments(postId, userId)
        }
    }

    fun addComment(userId: Long, postId: Long, comment: String) {
        viewModelScope.launch {
            val response = addCommentUseCase(userId, postId, comment)
            val currentState = _uiState.value
            if (currentState is CommentsState.Success) {
                getComments(postId, userId)
            } else {
                response.message?.let { Resource.error(it, null) }
            }
        }
    }

    fun removeComment(userId: Long, commentId: Long, postId: Long) {
        viewModelScope.launch {

            val response = removeCommentUseCase(userId, commentId)

            val currentState = _uiState.value
            if (currentState is CommentsState.Success) {
                // обновить комментарии
                getComments(postId, userId)

            } else {
                response.message?.let { Resource.error(it, null) }
            }
        }
    }

}


