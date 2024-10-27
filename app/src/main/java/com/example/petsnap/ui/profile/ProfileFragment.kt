package com.example.petsnap.ui.profile

import PostsAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petsnap.R
import com.example.petsnap.databinding.FragmentProfileBinding
import com.example.petsnap.domain.model.UserProfile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding ?: throw IllegalStateException("FragmentProfileBinding is not initialized")

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var postsAdapter: PostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()

        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getLong("user_id", -1L)

        if (userId != -1L) {
            viewModel.loadUserProfile(userId) // Загружаем профиль пользователя
        } else {
            Toast.makeText(requireContext(), "User ID not found", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerViewPosts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                // Условия для загрузки следующих постов
                if (!viewModel.isLoading && !viewModel.isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 1 && // изменено на totalItemCount - 1
                        firstVisibleItemPosition >= 0 &&
                        totalItemCount >= viewModel.pageSize
                    ) {
                        viewModel.loadMorePosts()  // Загружаем следующую страницу
                    }
                }
            }
        })
    }

    private fun setupRecyclerView() {
        postsAdapter = PostsAdapter(listOf())
        binding.recyclerViewPosts.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = postsAdapter
        }
    }

    private fun setupObservers() {
        viewModel.userProfile.observe(viewLifecycleOwner) { userProfile ->
            userProfile?.let { updateUserUI(it) }
        }

        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            posts?.let {
                postsAdapter.updatePosts(it)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadUserProfile() {
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getLong("user_id", -1L)

        if (userId != -1L) {
            viewModel.loadUserProfile(userId)
        } else {
            Toast.makeText(requireContext(), "User ID not found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserProfile() // Перезагрузка профиля при возврате на фрагмент
    }

    private fun updateUserUI(userProfile: UserProfile) {
        binding.usernameTextView.text = userProfile.username
        binding.bioTextView.text = userProfile.bio ?: "No bio available"

        // Загрузка аватара с помощью Glide
        Glide.with(this@ProfileFragment)
            .load(userProfile.avatar)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.mipmap.ic_launcher)
            .into(binding.avatarImageView)

        // Обновляем список постов в адаптере
        postsAdapter.updatePosts(userProfile.posts)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
