package com.example.petsnap.domain.model

data class CommentsRequest(
    val postId: Long,
    val userId: Long
)
