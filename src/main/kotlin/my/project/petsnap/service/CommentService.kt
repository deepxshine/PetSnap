package my.project.petsnap.service

import my.project.petsnap.entity.CommentDB
import my.project.petsnap.repository.CommentRepository
import org.springframework.stereotype.Service

@Service
class CommentService(private val commentRepository: CommentRepository) {

    fun addComment(comment: CommentDB): CommentDB {
        return commentRepository.save(comment)
    }

    fun removeComment(commentId: Long) {
        commentRepository.deleteById(commentId)
    }

    fun getCommentsByPostId(postId: Long): List<CommentDB> {
        return commentRepository.findCommentsByPostId(postId)
    }
}