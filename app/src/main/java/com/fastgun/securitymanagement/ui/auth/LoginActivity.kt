package com.fastgun.securitymanagement.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fastgun.securitymanagement.R
import com.fastgun.securitymanagement.ui.dashboard.DashboardActivity
import com.fastgun.securitymanagement.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        emailEditText = findViewById(R.id.email_input)
        passwordEditText = findViewById(R.id.password_input)
        loginButton = findViewById(R.id.login_button)
        progressBar = findViewById(R.id.progress_bar)
        errorTextView = findViewById(R.id.error_text)

        // Set up click listener
        loginButton.setOnClickListener {
            performLogin()
        }

        // Observe authentication state
        observeAuthState()
    }

    private fun performLogin() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString()

        // Validation
        if (email.isEmpty() || password.isEmpty()) {
            showError("Please enter email and password")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Please enter a valid email address")
            return
        }

        if (password.length < 6) {
            showError("Password must be at least 6 characters")
            return
        }

        // Show loading state
        showLoading(true)

        // Perform login
        lifecycleScope.launch {
            authViewModel.login(email, password)
        }
    }

    private fun observeAuthState() {
        lifecycleScope.launch {
            authViewModel.authState.collect { state ->
                when (state) {
                    is AuthViewModel.AuthState.Loading -> {
                        showLoading(true)
                        clearError()
                    }
                    is AuthViewModel.AuthState.Success -> {
                        showLoading(false)
                        Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                        navigateToDashboard()
                    }
                    is AuthViewModel.AuthState.Error -> {
                        showLoading(false)
                        showError(state.message)
                    }
                    is AuthViewModel.AuthState.Idle -> {
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        loginButton.isEnabled = !isLoading
        emailEditText.isEnabled = !isLoading
        passwordEditText.isEnabled = !isLoading
    }

    private fun showError(message: String) {
        errorTextView.text = message
        errorTextView.visibility = android.view.View.VISIBLE
    }

    private fun clearError() {
        errorTextView.visibility = android.view.View.GONE
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
