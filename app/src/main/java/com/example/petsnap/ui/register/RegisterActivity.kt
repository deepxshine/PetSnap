package com.example.petsnap.ui.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.petsnap.databinding.ActivityRegisterBinding
import com.example.petsnap.ui.login.LoginActivity
import com.example.petsnap.utils.Status
import com.example.petsnap.utils.FileUtils
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding: ActivityRegisterBinding
        get() = _binding
            ?: throw IllegalStateException("ActivityRegisterBinding is not initialized")

    private val viewModel by viewModels<RegisterViewModel>()

    private var selectedFile: File? = null

    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedFile = FileUtils.createTempFileFromUri(this.contentResolver, it)
                binding.regAvatar.setImageURI(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.regSelectFileButton.setOnClickListener {
            filePickerLauncher.launch("image/*")
        }

        binding.regButton.setOnClickListener {
            val username = binding.regUsername.text.toString()
            val password = binding.regPassword.text.toString()
            val birthday = binding.regBirthday.text.toString()
            val bio = binding.regBio.text.toString()
            val file = selectedFile

            if (username.isEmpty()) {
                Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (file != null) {
                FileUtils.fileSizeLimit(this, file)
            }


            viewModel.registerUser(username, password, birthday, bio, file)
        }

        viewModel.registerResult.observe(this) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    val message = result.data?.message
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    // Navigate to the login screen
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                Status.ERROR -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }

                Status.LOADING -> {
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}






