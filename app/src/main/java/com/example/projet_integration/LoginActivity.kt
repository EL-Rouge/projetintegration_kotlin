package com.example.projet_integration

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.projet_integration.models.LoginRequest
import com.example.projet_integration.models.LoginResponse
import com.example.projet_integration.network.RetrofitInstance
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var errorTextView: TextView
    private lateinit var signUpButton: Button
    private lateinit var rootLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login) // Reference to your XML file

        // Initialize views
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginButton)
        errorTextView = findViewById(R.id.error)
        signUpButton = findViewById(R.id.signup)
        rootLayout = findViewById(R.id.root)

        // Login button click listener
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (validateForm(email, password)) {
                loginUser(email, password)
            } else {
                showError("Please fill in all fields")
            }
        }

        // Sign-Up button click listener
        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpStepOne::class.java)
            startActivity(intent)
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    private fun showError(message: String) {
        errorTextView.text = message
        errorTextView.visibility = View.VISIBLE
    }

    private fun loginUser(email: String, password: String) {
        // Show a loading indicator (optional)
        Snackbar.make(rootLayout, "Logging in...", Snackbar.LENGTH_SHORT).show()

        // Create the login request object
        val loginRequest = LoginRequest(email, password)

        // Make the API call
        RetrofitInstance.apiService.loginUser(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    Snackbar.make(rootLayout, "Login successful!", Snackbar.LENGTH_SHORT).show()

                    // Navigate to MainActivity
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)

                    // Finish the login activity
                    finish()
                } else {
                    showError("Login failed. Please check your email and password.")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showError("Error: ${t.message}")
            }
        })
    }
}
