package my.project.petsnap.dto

import my.project.petsnap.entity.CommentDB
import my.project.petsnap.entity.LikeDB
import my.project.petsnap.entity.PostDB

data class UserPageResponseDTO(
    val id: Long,
    val username: String,
    val avatar: String,
    val bio: String?,
    val posts: List<PostDB>,
    val likes: List<LikeDB>,
    val comments: List<CommentDB>
)
