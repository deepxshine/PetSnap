package my.project.petsnap.service

import jakarta.persistence.EntityNotFoundException
import my.project.petsnap.dto.CommentResponseDTO
import my.project.petsnap.dto.UserSearchResponseDTO
import my.project.petsnap.entity.CommentDB
import my.project.petsnap.repository.CommentRepository
import my.project.petsnap.repository.PostRepository
import my.project.petsnap.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
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
            comment = comment,
            commentTime = LocalDateTime.now(),
            post = post,
            user = user,
        )

        commentRepository.save(createdComment)

        val commentCreatedResponse = createCommentDTO(createdComment)

        return ResponseEntity.ok(mapOf("message" to "Comment added", "comment" to commentCreatedResponse))
    }

    fun removeComment(userId: Long, commentId: Long): ResponseEntity<Any> {
        userRepository.findById(userId).orElseThrow { EntityNotFoundException("User not found") }

        val comment = commentRepository.findById(commentId).orElseThrow { EntityNotFoundException("Comment not found") }

        val removeCommentResponse = createCommentDTO(comment)

        commentRepository.deleteById(commentId)
        return ResponseEntity.ok(mapOf("message" to "Comment removed", "comment" to removeCommentResponse))
    }

    fun getCommentsByPostId(postId: Long, userId: Long, page: Int, size: Int): ResponseEntity<Any> {

        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "commentTime"))
        val commentsPage = commentRepository.findCommentDBByPostIdOrderByCommentTimeDesc(postId, pageable)

        val commentsList = commentsPage.content.map { comment ->
            CommentResponseDTO(
                id = comment.id!!,
                comment = comment.comment,
                commentTime = comment.commentTime,
                user = UserSearchResponseDTO(
                    id = comment.user.id!!,
                    username = comment.user.username,
                    avatar = comment.user.avatar,
                ),
                commentedByUser = comment.user.id == userId
            )

        }

        return ResponseEntity.ok(mapOf("comments" to commentsList))
    }

    fun createCommentDTO(comment: CommentDB): CommentResponseDTO {
        return CommentResponseDTO(
            id = comment.id!!,
            comment = comment.comment,
            commentTime = comment.commentTime,
            user = UserSearchResponseDTO(
                id = comment.user.id!!,
                username = comment.user.username,
                avatar = comment.user.avatar
            ),
            commentedByUser = true,
        )

    }

}