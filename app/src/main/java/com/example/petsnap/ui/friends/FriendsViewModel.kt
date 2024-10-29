package com.example.petsnap.ui.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petsnap.domain.usecase.GetFriendshipUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val getFriendshipUseCase: GetFriendshipUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<FriendsScreenState> =
        MutableStateFlow(FriendsScreenState.Initial)
    val uiState: StateFlow<FriendsScreenState> = _uiState.asStateFlow()

    fun loadFriendship(userId: Long) {
        viewModelScope.launch {
            _uiState.value = FriendsScreenState.Loading(
                followersList = listOf(),
                followingsList = listOf()
            )
            try {
                getFriendshipUseCase(userId).collectLatest { friendship ->

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

    fun followUser() {
        //todo:
    }

    fun unfollowUser() {
        //todo:
    }
}