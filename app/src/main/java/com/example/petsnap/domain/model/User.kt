package com.example.petsnap.domain.model

data class User(
    val id: Long,
    val username: String,
    val avatar: String,
    val bio: String?,
)
