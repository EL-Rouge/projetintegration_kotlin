package com.example.projet_integration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.projet_integration.models.LoginRequest
import com.example.projet_integration.models.LoginResponse
import com.example.projet_integration.network.RetrofitInstance
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

    // Save the user ID in SharedPreferences
    private fun saveClientId(context: Context, clientId: String) {
        val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("CLIENT_ID", clientId)
            apply()
            Log.d("LoginActivity", "User ID saved: $clientId")
        }
    }

    // Form validation
    private fun validateForm(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    // Show error message
    private fun showError(message: String) {
        errorTextView.text = message
        errorTextView.visibility = View.VISIBLE
    }

    // Login user
    private fun loginUser(email: String, password: String) {
        // Check if the user is trying to log in as an admin
        if (email == "admin@admin.com" && password == "adminpassword") {
            // If the credentials match the admin, proceed to the admin screen
            val intent = Intent(this@LoginActivity, AdminDashboardActivity::class.java) // Admin activity
            startActivity(intent)
        } else {
            // Proceed with the network login request for regular users
            val loginData = LoginRequest(email, password)
            RetrofitInstance.apiService.loginUser(loginData).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        loginResponse?.user?.userId?.let {
                            Log.d("LoginActivity", "Received User ID: $it")
                            saveClientId(this@LoginActivity, it)
                        } ?: Log.e("LoginActivity", "User ID is null")
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.e("LoginActivity", "Login failed: ${response.errorBody()?.string()}")
                        showError("Invalid credentials")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("LoginActivity", "Login error: ${t.message}")
                    showError("An error occurred during login")
                }
            })
        }
    }
}
