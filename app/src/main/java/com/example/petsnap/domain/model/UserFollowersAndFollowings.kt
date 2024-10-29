package com.example.petsnap.domain.model

data class UserFollowersAndFollowings(
    val followersList: List<FriendshipResponse>,
    val followingsList: List<FriendshipResponse>
)
