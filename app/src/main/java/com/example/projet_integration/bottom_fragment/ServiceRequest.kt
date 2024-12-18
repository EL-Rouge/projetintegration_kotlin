package com.example.projet_integration

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_integration.adapter.ServiceRequestAdapter
import com.example.projet_integration.models.ServiceRequest
import com.example.projet_integration.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceRequest : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var serviceRequestAdapter: ServiceRequestAdapter
    private val serviceRequestList = mutableListOf<ServiceRequest>()


    // Inflate the fragment layout and initialize views
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment's layout
        val view = inflater.inflate(R.layout.fragment_list_service_request, container, false)


        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.service_requests_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize Adapter
        serviceRequestAdapter = ServiceRequestAdapter(requireContext(), serviceRequestList)
        recyclerView.adapter = serviceRequestAdapter

        // Fetch data from API
        fetchServiceRequests()

        return view


    }

    // Fetch data from your backend using Retrofit
    private fun fetchServiceRequests() {
        val call = RetrofitInstance.apiService.getAllServices()
        call.enqueue(object : Callback<List<ServiceRequest>> {
            override fun onResponse(call: Call<List<ServiceRequest>>, response: Response<List<ServiceRequest>>) {
                if (response.isSuccessful) {
                    // Log the raw response for debugging
                    Log.d("ServiceRequest", "Response: ${response.body()}")

                    response.body()?.let {
                        serviceRequestList.clear()
                        serviceRequestList.addAll(it)
                        serviceRequestAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.d("ServiceRequest", "Response: ${response.body()}")
    
                    Log.e("ServiceRequest", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(call: Call<List<ServiceRequest>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
