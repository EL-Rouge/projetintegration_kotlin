package com.example.projet_integration

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.projet_integration.R
import com.example.projet_integration.models.User
import com.example.projet_integration.network.RetrofitInstance
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpStepOne : AppCompatActivity() {

    lateinit var selectAccountType: Button
    lateinit var selectedAccountType: TextView
    lateinit var signupbtn: Button
    lateinit var Name: EditText
    lateinit var Email: EditText
    lateinit var Password: EditText
    lateinit var NameLayout: TextInputLayout
    lateinit var EmailLayout: TextInputLayout
    lateinit var PasswordLayout: TextInputLayout

    var accountType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_sign_up_step_one)

        // Initialize Views
        selectAccountType = findViewById(R.id.selectAccountType)
        selectedAccountType = findViewById(R.id.selectedAccountType)
        signupbtn = findViewById(R.id.signupbtn)
        Name = findViewById(R.id.Name)
        Email = findViewById(R.id.Email)
        Password = findViewById(R.id.Password)
        NameLayout = findViewById(R.id.NameLayout)
        EmailLayout = findViewById(R.id.EmailLayout)
        PasswordLayout = findViewById(R.id.PasswordLayout)

        // Handle Account Type Selection
        selectAccountType.setOnClickListener {
            showAccountTypeDialog()
        }

        // Handle Sign Up Button
        signupbtn.setOnClickListener {
            if (validateInputs()) {
                createUser()
            }
        }
    }

    private fun showAccountTypeDialog() {
        val accountTypes = arrayOf("Client Account", "Freelancer Account")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Account Type")
        builder.setItems(accountTypes) { _, which ->
            accountType = accountTypes[which]
            selectedAccountType.text = "Selected Account Type: $accountType"
        }
        builder.show()
    }

    private fun validateInputs(): Boolean {
        val isNameValid = validateName()
        val isEmailValid = validateEmail()
        val isPasswordValid = validatePassword()

        if (!isNameValid || !isEmailValid || !isPasswordValid) {
            return false
        }

        if (accountType == null) {
            Snackbar.make(findViewById(R.id.root), "Please select an account type", Snackbar.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun validateName(): Boolean {
        val name = Name.text.toString()
        return if (name.isEmpty()) {
            NameLayout.error = "Name is required"
            false
        } else {
            NameLayout.error = null
            true
        }
    }

    private fun validateEmail(): Boolean {
        val email = Email.text.toString()
        return if (email.isEmpty()) {
            EmailLayout.error = "Email is required"
            false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            EmailLayout.error = "Enter a valid email address"
            false
        } else {
            EmailLayout.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        val password = Password.text.toString()
        return if (password.isEmpty()) {
            PasswordLayout.error = "Password is required"
            false
        } else if (password.length < 8) {
            PasswordLayout.error = "Password must be at least 8 characters long"
            false
        } else {
            PasswordLayout.error = null
            true
        }
    }

    private fun createUser() {
        val user = User(
            username = Name.text.toString(),
            email = Email.text.toString(),
            password = Password.text.toString(),
            id = 0, // ID will be generated on the server or in the mock
            accounttype = accountType ?: "regular"
        )

        // Call the mock API
        RetrofitInstance.apiService.createUser(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Snackbar.make(
                        findViewById(R.id.root),
                        "Account Created: ${response.body()?.username}",
                        Snackbar.LENGTH_LONG
                    ).show()
                    // Navigate to LoginActivity
                    val intent = Intent(this@SignUpStepOne, LoginActivity::class.java)
                    startActivity(intent)
                    finish() // Optionally close SignUpStepOne to prevent going back to it
                } else {
                    Snackbar.make(findViewById(R.id.root), "Failed to create account", Snackbar.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Snackbar.make(findViewById(R.id.root), "Error: ${t.message}", Snackbar.LENGTH_LONG).show()
            }
        })
    }
}
