package com.example.petsnap.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.auth0.android.jwt.JWT
import com.example.petsnap.databinding.ActivityLoginBinding
import com.example.petsnap.ui.MainActivity
import com.example.petsnap.ui.register.RegisterActivity
import com.example.petsnap.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding: ActivityLoginBinding
        get() = _binding ?: throw IllegalStateException("ActivityLoginBinding is not initialized")

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val username = binding.loginUsername.text.toString()
            val password = binding.loginPassword.text.toString()

            viewModel.login(username, password)
        }

        binding.registerButton.setOnClickListener {
            // Navigate to the register screen
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Подписка на результат авторизации
        viewModel.loginResult.observe(this, Observer { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                    // получить userId
                    val userId = result.data?.userId!!

                    // сохранить userId
                    val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putLong("user_id", userId)
                    editor.apply()

                    // После успешного логина закрываем LoginActivity, запускаем MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                Status.ERROR -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    // todo: Handle loading state
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
