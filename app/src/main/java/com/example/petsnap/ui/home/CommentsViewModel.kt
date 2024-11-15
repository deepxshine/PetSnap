package com.example.petsnap.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.petsnap.domain.model.CommentsRequest
import com.example.petsnap.domain.model.CommentsResponse
import com.example.petsnap.domain.usecase.GetPostCommentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val getPostCommentsUseCase: GetPostCommentsUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<CommentsState> =
        MutableStateFlow(CommentsState.Initial)
    val uiState: StateFlow<CommentsState> = _uiState.asStateFlow()

    private suspend fun getComments(
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

    fun loadComments(postId: Long, userId: Long) {
        viewModelScope.launch {

            _uiState.value = CommentsState.Loading(
                comments = mutableMapOf(),
            )

            try {
                val commentsRequest = CommentsRequest(postId, userId)
                getComments(commentsRequest).collect { comments ->
                    _uiState.value = CommentsState.Success(
                        comments = mutableMapOf(postId to comments)
                    )
                }

            } catch (e: Exception) {
                _uiState.value = CommentsState.Error(e.message ?: "Unknown error")
            }

        }
    }

}


