package com.example.projet_integration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_integration.R
import com.example.projet_integration.adapter.ServiceRequestAdapter
import com.example.projet_integration.models.ServiceRequest
import com.example.projet_integration.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceRequest : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var serviceRequestAdapter: ServiceRequestAdapter
    private lateinit var searchView: SearchView
    private val serviceRequestList = mutableListOf<ServiceRequest>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_service_request, container, false)

        recyclerView = view.findViewById(R.id.service_requests_recycler_view)
        searchView = view.findViewById(R.id.search_bar)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        serviceRequestAdapter = ServiceRequestAdapter(requireContext(), serviceRequestList)
        recyclerView.adapter = serviceRequestAdapter

        fetchServiceRequests()

        // Handle search query
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchServiceRequests(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Optionally, you can handle live search here
                return false
            }
        })

        return view
    }

    private fun fetchServiceRequests() {
        val call = RetrofitInstance.apiService.getAllServices()
        call.enqueue(object : Callback<List<ServiceRequest>> {
            override fun onResponse(call: Call<List<ServiceRequest>>, response: Response<List<ServiceRequest>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        serviceRequestList.clear()
                        serviceRequestList.addAll(it)
                        serviceRequestAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e("ServiceRequest", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ServiceRequest>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun searchServiceRequests(description: String) {
        val call = RetrofitInstance.apiService.searchServiceRequests(description)
        call.enqueue(object : Callback<List<ServiceRequest>> {
            override fun onResponse(call: Call<List<ServiceRequest>>, response: Response<List<ServiceRequest>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        serviceRequestList.clear()
                        serviceRequestList.addAll(it)
                        serviceRequestAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e("ServiceRequest", "Search Error: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Failed to search data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ServiceRequest>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
