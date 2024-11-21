package com.example.petsnap.domain.usecase

import com.example.petsnap.domain.model.ResponseMsg
import com.example.petsnap.domain.repository.CommentRepository
import com.example.petsnap.utils.Resource
import javax.inject.Inject

class RemoveCommentUseCase @Inject constructor(private val commentRepository: CommentRepository) {

    suspend operator fun invoke(userId: Long, commentId: Long): Resource<ResponseMsg> {
        return commentRepository.removeComment(userId, commentId)
    }

}