package com.example.projet_integration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.projet_integration.databinding.FragmentServiceRequestEditBinding
import com.example.projet_integration.models.ServiceRequest
import com.example.projet_integration.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceRequestEditFragment : Fragment() {

    private lateinit var binding: FragmentServiceRequestEditBinding
    private lateinit var serviceRequest: ServiceRequest

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentServiceRequestEditBinding.inflate(inflater, container, false)

        // Retrieve the service request passed from the previous fragment
        serviceRequest = arguments?.getParcelable("serviceRequest")!!

        // Populate the fields with the service request data
        binding.serviceNameEditText.setText(serviceRequest.description)
        binding.statusEditText.setText(serviceRequest.status)
        binding.paymentStatusEditText.setText(serviceRequest.paymentStatus)

        // Set up the save button
        binding.saveButton.setOnClickListener {
            updateServiceRequest()
        }

        return binding.root
    }

    private fun updateServiceRequest() {
        // Update the service request object with data from the form
        val updatedServiceRequest = serviceRequest.copy(
            description = binding.serviceNameEditText.text.toString(),
            status = binding.statusEditText.text.toString(),
            paymentStatus = binding.paymentStatusEditText.text.toString()
        )

        // Pass the serviceRequest.id as a String, as expected by the API
        val serviceRequestId = updatedServiceRequest.id

        // Call the API to update the service request
        RetrofitInstance.apiService.updateServiceRequest(serviceRequestId, updatedServiceRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Service request updated", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack() // Go back to the previous fragment
                } else {
                    Toast.makeText(requireContext(), "Failed to update service request", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
