package com.example.projet_integration

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.projet_integration.network.RetrofitInstance
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private var clientId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Retrieve the client ID from SharedPreferences
        val sharedPref = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        clientId = sharedPref.getString("CLIENT_ID", null)
        Log.d("HomeFragment", "Retrieved Client ID: $clientId")

        if (clientId.isNullOrEmpty()) {
            Toast.makeText(context, "Client ID not found. Please log in again.", Toast.LENGTH_SHORT).show()
        }

        // Handle bottom navigation
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_category -> {
                    replaceFragment(CategoryFragment())
                    activity?.title = "Category"
                }
                R.id.bottom_history -> {
                    replaceFragment(HistoryFragment())
                    activity?.title = "History"
                }
                R.id.bottom_servicerequest -> {
                    replaceFragment(ServiceRequest())
                    activity?.title = "Notification"
                }
                R.id.bottom_cart -> {
                    replaceFragment(CartFragment())
                    activity?.title = "Cart"
                }
            }
            true
        }

        // Default fragment
        replaceFragment(CategoryFragment())
        activity?.title = "Category"
        bottomNavigationView.selectedItemId = R.id.bottom_category

        // Handle FloatingActionButton click
        val addFab = view.findViewById<FloatingActionButton>(R.id.addFabBtn)
        addFab.setOnClickListener {
            showServiceDescriptionDialog()
        }

        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.bottomFragment, fragment)
            .commit()
    }

    private fun showServiceDescriptionDialog() {
        // Inflate the custom dialog view
        val dialogView = LayoutInflater.from(context).inflate(R.layout.add_service_client, null)
        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)

        val alertDialog = builder.show()

        // Bind views from the dialog
        val closeButton: View = dialogView.findViewById(R.id.btnCloseDialog)
        val postRequestButton: View = dialogView.findViewById(R.id.btnPostRequest)
        val serviceStatusSpinner = dialogView.findViewById<Spinner>(R.id.spinnerServiceStatus)
        val serviceDescriptionEditText = dialogView.findViewById<EditText>(R.id.etServiceDescription)
        val servicePriceEditText = dialogView.findViewById<EditText>(R.id.etServicePrice)

        // Populate the Spinner with options
        val statusOptions = arrayOf("Pending", "In Progress", "Completed")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statusOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        serviceStatusSpinner.adapter = adapter

        // Close button action
        closeButton.setOnClickListener {
            alertDialog.dismiss()
        }

        // Post request button action
        postRequestButton.setOnClickListener {
            val status = serviceStatusSpinner.selectedItem.toString()
            val description = serviceDescriptionEditText.text.toString()
            val price = servicePriceEditText.text.toString()

            // Validate fields
            if (description.isBlank() || price.isBlank()) {
                Toast.makeText(context, "All fields are required.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (clientId.isNullOrEmpty()) {
                Toast.makeText(context, "Client ID is missing. Please log in again.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Post the service request
            postServiceRequest(
                com.example.projet_integration.models.ServiceRequest(
                    clientId = clientId!!,  // Use the retrieved client ID
                    description = description,
                    status = status,
                    paymentStatus = "Unpaid"
                )
            )

            // Dismiss the dialog
            alertDialog.dismiss()
        }
    }

    private fun postServiceRequest(serviceRequest: com.example.projet_integration.models.ServiceRequest) {
        val apiService = RetrofitInstance.apiService
        val call = apiService.createService(serviceRequest)

        // Log the request body
        Log.d("ServiceRequest", "Request Body: ${serviceRequest.toString()}")

        // Enqueue the call to execute it asynchronously
        call.enqueue(object : retrofit2.Callback<com.example.projet_integration.models.ServiceRequest> {
            override fun onResponse(
                call: retrofit2.Call<com.example.projet_integration.models.ServiceRequest>,
                response: retrofit2.Response<com.example.projet_integration.models.ServiceRequest>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Service posted successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        context,
                        "Failed to post service: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("ServiceRequest", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(
                call: retrofit2.Call<com.example.projet_integration.models.ServiceRequest>,
                t: Throwable
            ) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("ServiceRequest", "Error: ${t.message}")
            }
        })
    }
}
