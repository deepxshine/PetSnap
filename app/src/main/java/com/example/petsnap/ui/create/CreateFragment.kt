package com.example.petsnap.ui.create

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.petsnap.R
import com.example.petsnap.databinding.FragmentCreateBinding
import com.example.petsnap.utils.FileUtils
import com.example.petsnap.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class CreateFragment : Fragment() {
    private var _binding: FragmentCreateBinding? = null
    private val binding: FragmentCreateBinding
        get() = _binding ?: throw IllegalStateException("FragmentCreateBinding is not initialized")

    private val viewModel: CreateViewModel by viewModels()

    private var selectedFile: File? = null

    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedFile =
                    FileUtils.createTempFileFromUri(requireContext().contentResolver, it)
                binding.addPostImage.setImageURI(uri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        binding.addPostImage.setOnClickListener {
            filePickerLauncher.launch("image/*") // начать обработать загруженное фото
        }

        binding.confirmCreateButton.setOnClickListener {
            val file = selectedFile
            val text = binding.editPostText.text.toString()

            if (text.length > 255) {
                Toast.makeText(requireContext(), "Your text is too long", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val sharedPreferences =
                requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getLong("user_id", -1L)

            if (file == null) {
                Toast.makeText(requireContext(), "Photo cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                FileUtils.fileSizeLimit(requireContext(), file)
            }

            viewModel.createPost(file, text, userId)

            binding.editPostText.text.clear()
            binding.addPostImage.setImageResource(R.mipmap.ic_add)

        }

        binding.cancelCreateButton.setOnClickListener {
            return@setOnClickListener // back to the page before
        }


    }


    private fun setupObservers() {
        viewModel.newPost.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    successStateView()
                    val message = "Post created successfully"
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

                    // Navigate to the homeFragment screen
//                    findNavController().navigate(R.id.navigation_home)

                }

                Status.ERROR -> {
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                }

                Status.LOADING -> {
                    loadingStateView()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}