package com.example.projet_integration

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.projet_integration.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

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
                Snackbar.make(
                    findViewById(R.id.root),
                    "Account Created: $accountType",
                    Snackbar.LENGTH_LONG
                ).show()
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
}


