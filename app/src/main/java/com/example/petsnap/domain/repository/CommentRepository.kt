package com.example.petsnap.domain.repository

import androidx.paging.PagingSource
import com.example.petsnap.domain.model.CommentsResponse
import com.example.petsnap.domain.model.CommentsRequest

interface CommentRepository {
    suspend fun getPostComments(commentsRequest: CommentsRequest): PagingSource<Int, CommentsResponse>

    suspend fun addComment()

    suspend fun deleteComment()
}