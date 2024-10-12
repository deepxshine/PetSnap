package my.project.petsnap.dto

import java.time.LocalDateTime

data class CommentResponseDTO(
    val id: Long,
    val comment: String,
    val commentTime: LocalDateTime,
    val user: UserSearchResponseDTO,
    val commentedByUser: Boolean
)
