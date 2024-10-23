package com.example.petsnap.domain.model

import java.time.LocalDateTime

data class PostsOnMainPageResponse(
    val id: Long,
    val image: String,
    val text: String?,
    val user: UserSearchResponse,
    val postTime: String,
    val commentsCount: Int,
    val likesCount: Int,
    val likedByUser: Boolean
)