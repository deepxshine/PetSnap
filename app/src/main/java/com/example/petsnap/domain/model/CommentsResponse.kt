package com.example.petsnap.domain.model

data class CommentsResponse(
    val id: Long,
    val comment: String,
    val commentTime: String,
    val username: String,
    val commentedByUser: Boolean
)
