package com.example.petsnap.domain.repository

import androidx.paging.PagingSource
import com.example.petsnap.domain.model.CommentsRequest
import com.example.petsnap.domain.model.CommentsResponse
import com.example.petsnap.utils.Resource

interface CommentRepository {
    suspend fun getPostComments(commentsRequest: CommentsRequest): PagingSource<Int, CommentsResponse>

    suspend fun addComment(userId: Long, postId: Long, comment: String): Resource<CommentsResponse>

    suspend fun deleteComment()
}