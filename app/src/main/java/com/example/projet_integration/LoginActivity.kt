package com.example.projet_integration



import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

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

        // Initializing views
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginButton)
        errorTextView = findViewById(R.id.error)
        signUpButton = findViewById(R.id.signup)
        rootLayout = findViewById(R.id.root)



        // Set Login Button Click Listener
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (validateForm(email, password)) {
                if (mockApiCall(email, password)) {
                    Snackbar.make(rootLayout, "Login successful!", Snackbar.LENGTH_SHORT).show()
                    navigateToDashboard()
                } else {
                    showError("Invalid email or password")
                }
            } else {
                showError("Please fill in all fields")
            }
        }


        // Sign-Up Button Click Listener
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

    private fun navigateToDashboard() {
        // Intent to navigate to dashboard
        Toast.makeText(this, "Navigating to Dashboard...", Toast.LENGTH_SHORT).show()
    }

    private fun mockApiCall(email: String, password: String): Boolean {
        // Simulate a login with dummy credentials
        return email == "haider@gmail.com" && password == "haider"
    }

}
