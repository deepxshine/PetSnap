package my.project.petsnap.dto

import my.project.petsnap.entity.CommentDB
import my.project.petsnap.entity.LikeDB
import java.time.LocalDateTime

data class PostResponseDTO(
    val id: Long,
    val image: String,
    val text: String?,
    val postTime: LocalDateTime,
    val comments: List<CommentDB>?,
    val likes: List<LikeDB>?
)
