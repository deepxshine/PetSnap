package com.example.petsnap.domain.usecase

import com.example.petsnap.domain.model.CommentsResponse
import com.example.petsnap.domain.repository.CommentRepository
import com.example.petsnap.utils.Resource
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(private val commentRepository: CommentRepository) {

    suspend operator fun invoke(userId: Long, postId: Long, comment: String): Resource<CommentsResponse> {
        return commentRepository.addComment(userId, postId, comment)
    }

}