package com.example.petsnap.ui.profile

import PostsAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petsnap.R
import com.example.petsnap.databinding.FragmentProfileBinding
import com.example.petsnap.domain.model.PostOnProfile
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

        // Инициализация адаптер и RecyclerView
        postsAdapter = PostsAdapter(listOf())
        binding.recyclerViewPosts.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = postsAdapter
        }

        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getLong("user_id", -1L)

        if (userId != -1L) {
            viewModel.loadUserProfile(userId)
        } else {
            Toast.makeText(requireContext(), "User ID not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        postsAdapter = PostsAdapter(listOf())
        binding.recyclerViewPosts.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = postsAdapter
        }
    }

    private fun setupObservers() {
        // Подписка на данные профиля пользователя
        viewModel.userProfile.observe(viewLifecycleOwner) { userProfile ->
            userProfile?.let {
                updateUserUI(it)
                postsAdapter.updatePosts(it.posts) // Обновляем посты в адаптере
            }
        }

        // Подписка на ошибки
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

        // Функция для обновления UI с данными пользователя
        private fun updateUserUI(userProfile: UserProfile) {
            binding.usernameTextView.text = userProfile.username
            binding.bioTextView.text = userProfile.bio ?: "No bio available"

            // Загрузка аватара с помощью Glide
            Glide.with(this@ProfileFragment)
                .load(userProfile.avatar)
                .placeholder(R.drawable.ic_launcher_foreground) // Placeholder для загрузки
                .error(R.mipmap.ic_launcher) // Изображение на случай ошибки
                .into(binding.avatarImageView)

            // Обновляем список постов в адаптере
            postsAdapter.updatePosts(userProfile.posts)
        }

        // Освобождаем binding, чтобы избежать утечек памяти
        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
}
