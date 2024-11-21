package com.example.petsnap.ui.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petsnap.domain.model.FollowAndUnfollowRequest
import com.example.petsnap.domain.usecase.FollowUseCase
import com.example.petsnap.domain.usecase.GetFriendshipUseCase
import com.example.petsnap.domain.usecase.UnfollowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val getFriendshipUseCase: GetFriendshipUseCase,
    private val followUseCase: FollowUseCase,
    private val unfollowUseCase: UnfollowUseCase,
) : ViewModel() {
    private var userId: Long = -1L

    private val _uiState: MutableStateFlow<FriendsScreenState> =
        MutableStateFlow(FriendsScreenState.Initial)
    val uiState: StateFlow<FriendsScreenState> = _uiState.asStateFlow()

    fun loadFriendship(userId: Long) {
        this.userId = userId
        viewModelScope.launch {
            _uiState.value = FriendsScreenState.Loading(
                followersList = listOf(),
                followingsList = listOf()
            )
            try {
                getFriendshipUseCase(userId).collect { friendship ->

                    _uiState.value = FriendsScreenState.Success(
                        followersList = friendship.followersList,
                        followingsList = friendship.followingsList
                    )
                }
            } catch (e: Exception) {
                _uiState.value = FriendsScreenState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun followUser(followingId: Long) {
        val followerId = this.userId

        viewModelScope.launch {

            val followRequest = FollowAndUnfollowRequest(followerId, followingId)
            followUseCase(followRequest)

            val currentState = _uiState.value
            if (currentState is FriendsScreenState.Success) {
                try {
                    getFriendshipUseCase(followerId).collect { friendship ->

                        _uiState.value = FriendsScreenState.Success(
                            followersList = friendship.followersList,
                            followingsList = friendship.followingsList
                        )
                    }

                } catch (e: Exception) {

                    _uiState.value = FriendsScreenState.Error(e.message ?: "Unknown error")
                }
            }

        }

    }

    fun unfollowUser(followingId: Long) {

        val followerId = this.userId

        viewModelScope.launch {
            val unfollowRequest = FollowAndUnfollowRequest(followerId, followingId)
            unfollowUseCase(unfollowRequest)

            val currentState = _uiState.value

            if (currentState is FriendsScreenState.Success) {

                try {
                    getFriendshipUseCase(followerId).collect { friendship ->

                        _uiState.value = FriendsScreenState.Success(
                            followersList = friendship.followersList,
                            followingsList = friendship.followingsList
                        )
                    }

                } catch (e: Exception) {
                    _uiState.value = FriendsScreenState.Error(e.message ?: "Unknown error")
                }
            }

        }

    }
}