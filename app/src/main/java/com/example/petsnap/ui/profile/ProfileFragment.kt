package com.example.petsnap.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.petsnap.R
import com.example.petsnap.databinding.FragmentProfileBinding
import com.example.petsnap.domain.model.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding ?: throw IllegalStateException("FragmentProfileBinding is not initialized")

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getLong("user_id", -1L)

        if (userId == -1L) {
            Toast.makeText(requireContext(), "User ID not found", Toast.LENGTH_SHORT).show()
            return
        } else {
            // Загружаем профиль пользователя
            viewModel.loadUserProfile(userId)
        }

        // Подписка на данные пользователя
        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                updateUserUI(it)
            }
        })
        // Подписка на ошибки
        viewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        })
        }

        // Функция для обновления UI с данными пользователя
        private fun updateUserUI(user: User) {
            // Используем binding для доступа к элементам UI
            binding.usernameTextView.text = user.username
            binding.bioTextView.text = user.bio ?: "No bio available"

            // Загрузка аватара с помощью Glide
            Glide.with(this@ProfileFragment)
                .load(user.avatar)
                .placeholder(R.drawable.ic_launcher_foreground) // Placeholder для загрузки
                .error(R.drawable.ic_notifications_black_24dp) // Изображение на случай ошибки
                .into(binding.avatarImageView)
        }

        // Освобождаем binding, чтобы избежать утечек памяти
        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
}
