package my.project.petsnap.service

import jakarta.persistence.EntityNotFoundException
import my.project.petsnap.dto.CommentResponseDTO
import my.project.petsnap.dto.UserSearchResponseDTO
import my.project.petsnap.entity.CommentDB
import my.project.petsnap.repository.CommentRepository
import my.project.petsnap.repository.PostRepository
import my.project.petsnap.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class CommentService(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
) {
    @Transactional
    fun addComment(userId: Long, postId: Long, comment: String): ResponseEntity<Any> {

        val user = userRepository.findById(userId).orElseThrow { EntityNotFoundException("User not found") }
        val post = postRepository.findById(postId).orElseThrow { EntityNotFoundException("Post not found") }

        val createdComment = CommentDB(
            comment = comment.trimIndent(),
            commentTime = LocalDateTime.now(),
            post = post,
            user = user,
        )

        commentRepository.save(createdComment)

        val commentCreatedResponse = CommentResponseDTO(
            id = createdComment.id!!,
            comment = createdComment.comment,
            commentTime = createdComment.commentTime,
            username = createdComment.user.username,
            commentedByUser = true,
        )

        return ResponseEntity.ok(commentCreatedResponse)
    }

    fun removeComment(userId: Long, commentId: Long): ResponseEntity<Any> {
        userRepository.findById(userId).orElseThrow { EntityNotFoundException("User not found") }

        val comment = commentRepository.findById(commentId).orElseThrow { EntityNotFoundException("Comment not found") }

        return if (comment.user.id != userId) {
            ResponseEntity.badRequest().body(("message" to "You cannot delete other users' comments"))
        } else {
            commentRepository.deleteById(commentId)
            ResponseEntity.ok(mapOf("message" to "Comment removed"))
        }
    }

    fun getCommentsByPostId(postId: Long, userId: Long, page: Int, size: Int): Page<CommentResponseDTO> {

        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "commentTime"))
        val commentsPage = commentRepository.findCommentDBByPostIdOrderByCommentTimeDesc(postId, pageable)

        return commentsPage.map { comment ->
            CommentResponseDTO(
                id = comment.id!!,
                comment = comment.comment,
                commentTime = comment.commentTime,
                username = comment.user.username,
                commentedByUser = comment.user.id == userId
            )

        }


    }

}