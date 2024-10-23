package com.example.petsnap.ui.home

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
import com.example.petsnap.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding: FragmentHomeBinding
        get() = _binding ?: throw IllegalStateException("FragmentHomeBinding is not initialized")

    private val viewModel by viewModels<HomeViewModel>()

    private val homeAdapter by lazy {
        HomeAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRV()
        observeUiState()
        loadPosts()
    }

    private fun setUpRV() {
        binding.rvFragmentHome.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = homeAdapter

            // TODO: homeAdapter.createPostClickListener
            // TODO: homeAdapter.likeClickListener
            // TODO: homeAdapter.commentClickListener
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is HomeScreenState.Error -> {
                            Toast.makeText(requireContext(), state.msg, Toast.LENGTH_LONG).show()
                        }
                        HomeScreenState.Initial -> {}
                        HomeScreenState.Loading -> {
                            loadingStateView()
                        }
                        is HomeScreenState.Success -> {
                            successStateView()
                            homeAdapter.submitData(state.posts)
                        }
                    }
                }
            }
        }
    }

    private fun loadingStateView() {
        binding.pbHome.visibility = View.VISIBLE
    }

    private fun successStateView() {
        binding.pbHome.visibility = View.GONE
    }

    private fun loadPosts() {
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getLong("user_id", -1L)

        if (userId == -1L) {
            Toast.makeText(requireContext(), "User ID not found", Toast.LENGTH_SHORT).show()
            return
        } else {

              viewModel.loadPosts(userId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}