package com.example.projet_integration

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_integration.adapters.ServiceAdapter2
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
        progressBar.visibility = View.VISIBLE

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
                        // Set up RecyclerView with the adapter and action handlers
                        adapter = ServiceAdapter2(services, ::handleUpdate, ::handleDelete)
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "No services found for this client.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to load services. Please try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<ServiceRequest>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("HistoryFragment", "Error fetching services: ${t.message}")
                Toast.makeText(
                    requireContext(),
                    "An error occurred: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun handleDelete(service: ServiceRequest) {
        val apiService = RetrofitInstance.apiService
        // Use service.id (mapped from MongoDB's _id)
        apiService.deleteServiceRequest(service.id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Service deleted successfully", Toast.LENGTH_SHORT).show()
                    fetchServices(getClientId(requireContext()) ?: "")
                } else {
                    Toast.makeText(requireContext(), "Failed to delete service", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("HistoryFragment", "Error deleting service: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun handleUpdate(service: ServiceRequest) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_update_service, null)
        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val editDescription = dialogView.findViewById<EditText>(R.id.edit_description)
        val editStatus = dialogView.findViewById<EditText>(R.id.edit_status)
        val editPaymentStatus = dialogView.findViewById<EditText>(R.id.edit_payment_status)
        val btnSave = dialogView.findViewById<Button>(R.id.btn_save)
        val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)

        // Pre-fill fields with existing data
        editDescription.setText(service.description)
        editStatus.setText(service.status)
        editPaymentStatus.setText(service.paymentStatus)

        // Handle Save button click
        btnSave.setOnClickListener {
            val updatedDescription = editDescription.text.toString()
            val updatedStatus = editStatus.text.toString()
            val updatedPaymentStatus = editPaymentStatus.text.toString()

            // Validate inputs
            if (updatedDescription.isBlank() || updatedStatus.isBlank() || updatedPaymentStatus.isBlank()) {
                Toast.makeText(requireContext(), "All fields must be filled", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Prepare the updates map
            val updates = mapOf(
                "description" to updatedDescription,
                "status" to updatedStatus,
                "paymentStatus" to updatedPaymentStatus
            )

            // Send the update request
            val apiService = RetrofitInstance.apiService
            apiService.updateServiceRequest(service.id, updates).enqueue(object : Callback<ServiceRequest> {
                override fun onResponse(call: Call<ServiceRequest>, response: Response<ServiceRequest>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Service updated successfully", Toast.LENGTH_SHORT).show()
                        fetchServices(getClientId(requireContext()) ?: "") // Refresh the list
                    } else {
                        Toast.makeText(context, "Failed to update service", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }

                override fun onFailure(call: Call<ServiceRequest>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            })
        }

        // Handle Cancel button click
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }







    // Retrieve Client ID from SharedPreferences
    private fun getClientId(context: Context): String? {
        val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("CLIENT_ID", null)
    }
}
