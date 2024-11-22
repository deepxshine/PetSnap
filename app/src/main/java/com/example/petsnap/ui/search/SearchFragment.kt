package com.example.petsnap.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petsnap.R
import com.example.petsnap.databinding.FragmentSearchBinding
import com.example.petsnap.ui.profile.UserProfileFragment
import com.example.petsnap.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var usersAdapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()

        // Устанавливаем слушатель кликов
        usersAdapter.setOnItemClickListener { user ->
            showUserProfile(user.id)
        }

        // Слушатель изменений текста для поиска
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    viewModel.setSearchQuery(it.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


    private fun setupRecyclerView() {
        usersAdapter = UsersAdapter(listOf())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = usersAdapter
        }
    }

    private fun setupObservers() {
        viewModel.searchResults.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.emptyTextView.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    val users = resource.data.orEmpty()
                    if (users.isEmpty() && binding.searchEditText.text.isNotEmpty()) {
                        binding.recyclerView.visibility = View.GONE
                        binding.emptyTextView.visibility = View.VISIBLE
                    } else {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.emptyTextView.visibility = View.GONE
                        usersAdapter.updateUsers(users)
                    }
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.emptyTextView.visibility = View.VISIBLE
                    binding.emptyTextView.text = resource.message
                }
            }
        }
    }

    // Показываем профиль пользователя внутри текущего фрагмента
    private fun showUserProfile(userId: Long) {
        findNavController().navigate(R.id.navigation_user_profile, Bundle().apply {
            putLong("userId", userId)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

