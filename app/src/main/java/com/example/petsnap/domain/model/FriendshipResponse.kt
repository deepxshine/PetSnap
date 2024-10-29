package com.example.petsnap.domain.model

data class FriendshipResponse(
    val id: Long,
    val username: String,
    val avatar: String?,
    var followedByUser: Boolean,
)
