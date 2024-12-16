package com.example.projet_integration.bottom_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.projet_integration.R
import com.example.projet_integration.models.ServiceRequest
import com.example.projet_integration.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceRequest : Fragment() {

    private lateinit var serviceDescriptionTextView: TextView
    private lateinit var fetchServiceButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment layout
        val view = inflater.inflate(R.layout.fragment_service_request, container, false)

        // Initialize the views
        serviceDescriptionTextView = view.findViewById(R.id.service_description)
        fetchServiceButton = view.findViewById(R.id.fetch_service_button)

        // Set up an OnClickListener for the button
        fetchServiceButton.setOnClickListener {
            // Example: Fetch a service request by ID (change this as needed)
            val serviceRequestId = 1  // Replace with the actual ID you want to query
            fetchServiceRequestById(serviceRequestId)
        }

        return view
    }

    // Fetch a service request by its ID
    private fun fetchServiceRequestById(serviceRequestId: Int) {
        val call = RetrofitInstance.apiService.getServiceById(serviceRequestId)
        call.enqueue(object : Callback<ServiceRequest> {
            override fun onResponse(call: Call<ServiceRequest>, response: Response<ServiceRequest>) {
                if (response.isSuccessful) {
                    val serviceRequest = response.body()
                    // Handle the retrieved service request
                    serviceRequest?.let {
                        // For example, update the TextView with the service description
                        serviceDescriptionTextView.text = "Service Description: ${it.description}"
                    }
                } else {
                    Log.e("ServiceRequest", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Failed to load service", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ServiceRequest>, t: Throwable) {
                Log.e("ServiceRequest", "Error: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
