package com.example.petsnap.ui.friends

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petsnap.databinding.FragmentFriendsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FriendsFragment : Fragment() {

    private var _binding: FragmentFriendsBinding? = null

    private val binding: FragmentFriendsBinding
        get() = _binding ?: throw IllegalStateException("FragmentFriendsBinding is not initialized")

    private val viewModel by viewModels<FriendsViewModel>()

    private val followersAdapter by lazy {
        FollowersAdapter(viewModel)
    }

    private val followingsAdapter by lazy {
        FollowingsAdapter(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsBinding.inflate(layoutInflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRV()

        binding.followerButton.setOnClickListener {
            println("click")
            binding.rvFragmentFriends.adapter = followersAdapter
            viewModel.uiState.value.let { state ->
                println("enter")
                if (state is FriendsScreenState.Success) {
                    println("${state.followersList}")
                    followersAdapter.submitList(state.followersList)
                }
            }
        }

        binding.followingButton.setOnClickListener {
            println("click")
            binding.rvFragmentFriends.adapter = followingsAdapter
            viewModel.uiState.value.let { state ->
                println("enter")
                if (state is FriendsScreenState.Success) {
                    println("${state.followingsList}")
                    followingsAdapter.submitList(state.followingsList)
                }
            }
        }


        observeUiState()
        loadFriendship()
    }

    private fun setUpRV() {
        binding.rvFragmentFriends.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = followersAdapter // показать followersAdapter по умолчанию

        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    when(state) {
                        is FriendsScreenState.Error -> {
                            Toast.makeText(requireContext(), state.msg, Toast.LENGTH_LONG).show()
                        }
                        is FriendsScreenState.Initial -> {}
                        is FriendsScreenState.Loading -> {
                            loadingStateView()
                        }
                        is FriendsScreenState.Success -> {
                            successStateView()
                            println("${state.followersList}")
                            followersAdapter.submitList(state.followersList) // показать followersList по умолчанию
                            followingsAdapter.submitList(state.followingsList)
                        }
                    }
                }
            }
        }
    }

    private fun getUserId(): Long {
        val sharedPreferences =
            requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getLong("user_id", -1L)
        return userId
    }

    private fun loadFriendship() {
        val userId = getUserId()
        viewModel.loadFriendship(userId)
    }

    private fun loadingStateView() {
        binding.pbHome.visibility = View.VISIBLE
    }

    private fun successStateView() {
        binding.pbHome.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}