package com.example.petsnap.domain.model

data class PostOnProfile(
    val id: Long,
    val image: String,
    val text: String?,
    val postTime: String,
    val commentsCount: Int,
    val likesCount: Int,
    val likedByUser: Boolean,
)
