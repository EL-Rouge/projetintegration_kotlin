package com.example.projet_integration.adapter

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_integration.ChatActivity
import com.example.projet_integration.R
import com.example.projet_integration.models.ServiceRequest
import com.example.projet_integration.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class ServiceRequestAdapter(
    private val context: Context,
    private val serviceRequests: MutableList<ServiceRequest> // Changed to MutableList to update the list
) : RecyclerView.Adapter<ServiceRequestAdapter.ServiceRequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceRequestViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_service_request, parent, false)
        return ServiceRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceRequestViewHolder, position: Int) {
        val serviceRequest = serviceRequests[position]

        // Static fields
        holder.status.text = serviceRequest.status
        holder.description.text = serviceRequest.description
        holder.paymentStatus.text = serviceRequest.paymentStatus

        // Dynamic client name fetching
        serviceRequest.clientId?.let { clientId ->
            holder.clientId.text = "Client: Fetching..."
            CoroutineScope(Dispatchers.Main).launch {
                val username = fetchUsernameById(clientId)
                holder.clientId.text = "Client: $username"
            }
        }

        // Set icon based on status
        holder.icon.setImageResource(
            when (serviceRequest.status) {
                "Completed" -> R.drawable.ic_completed
                "In Progress" -> R.drawable.ic_progress
                else -> R.drawable.ic_pending
            }
        )

        // Change the background color of the CardView based on the exported status
        if (serviceRequest.exported) {
            holder.bg.setCardBackgroundColor(Color.RED) // Red for exported = true
        } else {
            holder.bg.setCardBackgroundColor(Color.WHITE) // White for exported = false
        }

        holder.buttonContact.setOnClickListener {
            val clientId = serviceRequest.clientId // Get clientId from serviceRequest
            val freelancerId = getClientId(context) // Get the current freelancer's ID from the context (freelancer account)
            val serviceId = serviceRequest.id // Get the service ID from the serviceRequest

            Log.d("ServiceRequestAdapter", "clientId: $clientId, freelancerId: $freelancerId, serviceId: $serviceId") // Log for debugging

            if (clientId != null && freelancerId != null && serviceId != null) {
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("CLIENT_ID", clientId)
                intent.putExtra("FREELANCER_ID", freelancerId)
                intent.putExtra("SERVICE_ID", serviceId) // Add serviceId here
                context.startActivity(intent)

            } else {
                Toast.makeText(context, "Client ID, Freelancer ID, or Service ID is missing", Toast.LENGTH_SHORT).show()
            }
        }

        holder.buttonReport.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                reportServiceRequest(serviceRequest.id, position, holder)
            }
        }

        Log.d("Adapter", "ServiceRequest exported: ${serviceRequest.exported}")
    }


    override fun getItemCount(): Int = serviceRequests.size

    inner class ServiceRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.icon_status)
        val status: TextView = itemView.findViewById(R.id.status)
        val description: TextView = itemView.findViewById(R.id.description)
        val paymentStatus: TextView = itemView.findViewById(R.id.payment_status)
        val clientId: TextView = itemView.findViewById(R.id.client_id)
        val buttonContact: Button = itemView.findViewById(R.id.button_contact)
        val buttonReport: Button = itemView.findViewById(R.id.button_report)
        val bg: CardView = itemView.findViewById(R.id.card_view) // Reference to the CardView


    }

    private suspend fun fetchUsernameById(clientId: String): String {
        return try {
            val response = RetrofitInstance.apiService.getUserById(clientId)
            if (response.isSuccessful) {
                response.body()?.username ?: "Unknown User"
            } else {
                Log.e("ServiceRequestAdapter", "Error fetching username: ${response.errorBody()?.string()}")
                "Unknown User"
            }
        } catch (e: Exception) {
            Log.e("ServiceRequestAdapter", "Exception fetching username: ${e.message}")
            "Unknown User"
        }
    }

    private suspend fun reportServiceRequest(id: String, position: Int, holder: ServiceRequestViewHolder) {
        try {
            val response: Response<Unit> = RetrofitInstance.apiService.reportServiceRequest(id)
            if (response.isSuccessful) {
                Toast.makeText(context, "Reported successfully!", Toast.LENGTH_SHORT).show()

                // Update the service request as reported
                serviceRequests[position].exported = true
                holder.itemView.setBackgroundColor(Color.RED)
            } else {
                Log.e("ServiceRequestAdapter", "Error reporting service request: ${response.errorBody()?.string()}")
                Toast.makeText(context, "Failed to report service request.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("ServiceRequestAdapter", "Exception reporting service request: ${e.message}")
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to get the clientId from shared preferences (assuming it's stored there)
    private fun getClientId(context: Context): String? {
        val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("CLIENT_ID", null)
    }
}
