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

class UserEditFragment : Fragment() {

    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText // Add password field
    private lateinit var submitButton: Button
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_edit, container, false)

        // Initialize the UI components
        usernameEditText = view.findViewById(R.id.usernameEditText)
        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText) // Initialize password field
        submitButton = view.findViewById(R.id.submitButton)

        // Retrieve the User object passed via arguments
        val user = arguments?.getParcelable<User>("user")
        user?.let {
            userId = it.userId  // Save the userId for future use
            usernameEditText.setText(it.username)  // Pre-fill the username
            emailEditText.setText(it.email)    // Pre-fill the email
        }

        // Handle the submit button click
        submitButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Ensure the fields are not empty
            if (username.isNotEmpty() && email.isNotEmpty() && userId != null) {
                val updatedUser = User(
                    userId = userId!!,
                    username = username,
                    password = password,  // Add the password here
                    email = email,
                    accountType = "user"
                )
                updateUser(userId!!, updatedUser)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun updateUser(userId: String, user: User) {
        // Make API call to update the user
        RetrofitInstance.apiService.updateUser(userId, user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "User updated successfully", Toast.LENGTH_SHORT).show()

                    // Navigate back to the home page (UserListFragment)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.bottomFragmentAdmin, UserListFragment())  // Replace with UserListFragment (home page)
                        .addToBackStack(null) // Optional: Add to back stack to enable "Back" navigation
                        .commit()
                } else {
                    Toast.makeText(requireContext(), "Failed to update user: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
