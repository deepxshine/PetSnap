package com.example.petsnap.domain.model

data class FollowAndUnfollowRequest(
    val followerId: Long,
    val followingId: Long
)
