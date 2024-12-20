package com.example.projet_integration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_integration.databinding.FragmentUserListBinding
import com.example.projet_integration.adapter.UserAdapter
import com.example.projet_integration.models.User
import com.example.projet_integration.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListFragment : Fragment() {

    private lateinit var binding: FragmentUserListBinding
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using ViewBinding
        binding = FragmentUserListBinding.inflate(inflater, container, false)

        // Set up RecyclerView and Adapter
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(requireContext())
        userAdapter = UserAdapter({ user -> editUser(user) }, { user -> deleteUser(user) })
        binding.recyclerViewUsers.adapter = userAdapter

        // Fetch users data from the network
        fetchUsers()

        // Set up the Add User button click listener
        binding.addUserButton.setOnClickListener {
            // Handle the add user action (currently just a toast)
            Toast.makeText(requireContext(), "Add User functionality is under construction", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun fetchUsers() {
        binding.progressBar.visibility = View.VISIBLE  // Show the progress bar while loading

        RetrofitInstance.apiService.getAllUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                binding.progressBar.visibility = View.GONE  // Hide the progress bar once data is loaded

                if (response.isSuccessful) {
                    val users = response.body()
                    if (users != null) {
                        userAdapter.submitList(users)
                    } else {
                        Toast.makeText(requireContext(), "No users found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch users: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE  // Hide the progress bar on failure
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editUser(user: User) {
        // Navigate to UserEditFragment
        val bundle = Bundle().apply {
            putParcelable("user", user) // Passing the user object
        }
        val fragment = UserEditFragment()
        fragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.bottomFragmentAdmin, fragment) // Use your container ID here
            .addToBackStack(null)
            .commit()
    }


    private fun deleteUser(user: User) {
        // Call the API to delete the user
        RetrofitInstance.apiService.deleteUser(user.userId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "User deleted successfully", Toast.LENGTH_SHORT).show()
                    fetchUsers() // Refresh the user list
                } else {
                    Toast.makeText(requireContext(), "Failed to delete user: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
