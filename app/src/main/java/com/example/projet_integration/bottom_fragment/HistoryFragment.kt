package com.example.projet_integration

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_integration.adapter.ServiceAdapter2
import com.example.projet_integration.models.ServiceRequest
import com.example.projet_integration.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ServiceAdapter2
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            // Initialize RecyclerView and ProgressBar
            recyclerView = view.findViewById(R.id.recyclerView)
            progressBar = view.findViewById(R.id.progress_bar)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            // Get the client ID
            val clientId = getClientId(requireContext())  // Get the stored clientId
            Log.d("HistoryFragment", "Retrieved Client ID: $clientId")

            if (clientId != null) {
                // Fetch services using the client ID
                fetchServices(clientId)
            } else {
                // Handle case when clientId is null
                Toast.makeText(requireContext(), "No client ID found. Please log in again.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("HistoryFragment", "Error in onViewCreated: ${e.message}")
            Toast.makeText(requireContext(), "An error occurred while initializing the view.", Toast.LENGTH_SHORT).show()
        }
    }







    // Fetch services using Retrofit
    private fun fetchServices(clientId: String) {
        // Show the progress bar
        progressBar.visibility = View.VISIBLE

        // API call to fetch services by client ID
        val apiService = RetrofitInstance.apiService
        apiService.getServicesByClientId(clientId).enqueue(object : Callback<List<ServiceRequest>> {
            override fun onResponse(
                call: Call<List<ServiceRequest>>,
                response: Response<List<ServiceRequest>>
            ) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {
                    val services = response.body()!!
                    if (services.isNotEmpty()) {
                        // Set up RecyclerView with the adapter
                        adapter = ServiceAdapter2(services)
                        recyclerView.adapter = adapter
                    } else {
                        // No services found
                        Toast.makeText(
                            requireContext(),
                            "No services found for this client.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // API response failure
                    Toast.makeText(
                        requireContext(),
                        "Failed to load services. Please try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<ServiceRequest>>, t: Throwable) {
                progressBar.visibility = View.GONE
                // Log and show error message
                Log.e("HistoryFragment", "Error fetching services: ${t.message}")
                Toast.makeText(
                    requireContext(),
                    "An error occurred: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

        // Retrieve Client ID from SharedPreferences
        fun getClientId(context: Context): String? {
            val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            return sharedPref.getString("CLIENT_ID", null) // Retrieve the clientId
        }
}
