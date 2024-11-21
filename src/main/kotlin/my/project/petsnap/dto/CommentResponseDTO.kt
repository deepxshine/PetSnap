package my.project.petsnap.dto

import java.time.LocalDateTime

data class CommentResponseDTO(
    val id: Long,
    val comment: String,
    val commentTime: LocalDateTime,
    val username: String,
    val commentedByUser: Boolean,
    val postId: Long
)
