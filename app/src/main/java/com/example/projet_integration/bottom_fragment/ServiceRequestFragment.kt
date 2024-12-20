package com.example.projet_integration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projet_integration.adapter.AdminServiceRequestAdapter
import com.example.projet_integration.databinding.FragmentServiceRequestBinding
import com.example.projet_integration.models.ServiceRequest
import com.example.projet_integration.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceRequestFragment : Fragment() {

    private lateinit var adapter: AdminServiceRequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentServiceRequestBinding.inflate(inflater, container, false)

        // Initialize adapter
        adapter = AdminServiceRequestAdapter(
            onEditRequest = { serviceRequest -> onEditClicked(serviceRequest) },
            onDeleteRequest = { serviceRequest -> onDeleteClicked(serviceRequest) }
        )

        // Set up RecyclerView
        binding.recyclerViewServiceRequests.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewServiceRequests.adapter = adapter

        // Fetch service requests
        fetchServiceRequests()

        return binding.root
    }

    private fun fetchServiceRequests() {
        RetrofitInstance.apiService.getAllServices().enqueue(object : Callback<List<ServiceRequest>> {
            override fun onResponse(call: Call<List<ServiceRequest>>, response: Response<List<ServiceRequest>>) {
                if (response.isSuccessful) {
                    response.body()?.let { serviceRequests ->
                        adapter.submitList(serviceRequests)
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch service requests", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ServiceRequest>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun onEditClicked(serviceRequest: ServiceRequest) {
        val editFragment = ServiceRequestEditFragment().apply {
            arguments = Bundle().apply {
                putParcelable("serviceRequest", serviceRequest)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.bottomFragmentAdmin, editFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun onDeleteClicked(serviceRequest: ServiceRequest) {
        RetrofitInstance.apiService.deleteServiceRequest(serviceRequest.id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Service request deleted", Toast.LENGTH_SHORT).show()
                    fetchServiceRequests() // Refresh the list
                } else {
                    Toast.makeText(requireContext(), "Failed to delete service request", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
