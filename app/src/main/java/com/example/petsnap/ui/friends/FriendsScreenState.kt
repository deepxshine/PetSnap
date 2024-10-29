package com.example.petsnap.ui.friends

import com.example.petsnap.domain.model.FriendshipResponse

sealed class FriendsScreenState {

    data object Initial : FriendsScreenState()

    data class Loading(
        val followersList: List<FriendshipResponse> = listOf(),
        val followingsList: List<FriendshipResponse> = listOf()
    ) :
        FriendsScreenState()

    data class Success(
        val followersList: List<FriendshipResponse>,
        val followingsList: List<FriendshipResponse>
    ) : FriendsScreenState()

    data class Error(val msg: String) : FriendsScreenState()
}