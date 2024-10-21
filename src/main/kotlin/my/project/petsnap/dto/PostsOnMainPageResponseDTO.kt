package my.project.petsnap.dto

import java.time.LocalDateTime

data class PostsOnMainPageResponseDTO(
    val id: Long,
    val image: String,
    val text: String?,
    val user: UserSearchResponseDTO,
    val postTime: LocalDateTime,
// todo:    val comments: List<CommentResponseDTO>,

    val commentsCount: Int,
    val likesCount: Int,
    val likedByUser: Boolean,
)
