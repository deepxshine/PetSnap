package my.project.petsnap.dto

import my.project.petsnap.entity.CommentDB
import my.project.petsnap.entity.LikeDB
import my.project.petsnap.entity.PostDB

data class PostWithDetailsDTO(
    val post: PostDB,
    val likes: List<LikeDB>,
    val comments: List<CommentDB>
)
