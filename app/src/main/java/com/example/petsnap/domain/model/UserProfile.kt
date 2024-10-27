package com.example.petsnap.domain.model

data class UserProfile(
    val id: Long,
    val username: String?,
    val avatar: String?,
    val bio: String?,
    val posts: List<PostOnProfile>
)
