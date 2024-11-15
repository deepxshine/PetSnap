package com.example.petsnap.domain.model

data class PostsOnMainPageResponse(
    val id: Long,
    val image: String,
    val text: String?,
    val user: UserSearchResponse,
    val postTime: String,
    val commentsCount: Int,
    var likesCount: Int,
    var likedByUser: Boolean
)