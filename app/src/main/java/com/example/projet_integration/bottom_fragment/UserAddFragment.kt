package com.example.projet_integration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.projet_integration.R
import com.example.projet_integration.models.User
import com.example.projet_integration.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserAddFragment : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var accountTypeEditText: EditText
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_add, container, false)

        nameEditText = view.findViewById(R.id.nameEditText)
        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        accountTypeEditText = view.findViewById(R.id.accountTypeEditText)
        submitButton = view.findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val accountType = accountTypeEditText.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && accountType.isNotEmpty()) {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(requireContext(), "Invalid email format", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (password.length < 6) {
                    Toast.makeText(requireContext(), "Password should be at least 6 characters", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val newUser = User(
                    username = name,
                    password = password,
                    email = email,
                    accountType = accountType
                )
                createUser(newUser)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }


        return view
    }

    private fun createUser(user: User) {
        RetrofitInstance.apiService.createUser(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "User added successfully", Toast.LENGTH_SHORT).show()
                    // Clear fields after successful addition
                    nameEditText.text.clear()
                    emailEditText.text.clear()
                    passwordEditText.text.clear()
                    accountTypeEditText.text.clear()
                } else {
                    Toast.makeText(requireContext(), "Failed to add user: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
