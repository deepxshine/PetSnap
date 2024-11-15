package com.example.petsnap.domain.usecase

import androidx.paging.PagingSource
import com.example.petsnap.domain.model.CommentsRequest
import com.example.petsnap.domain.model.CommentsResponse
import com.example.petsnap.domain.repository.CommentRepository
import javax.inject.Inject

class GetPostCommentsUseCase @Inject constructor(private val commentRepository: CommentRepository){
    suspend operator fun invoke (commentsRequest: CommentsRequest) : PagingSource<Int, CommentsResponse> {
        val response = commentRepository.getPostComments(commentsRequest)
        return response
    }
}