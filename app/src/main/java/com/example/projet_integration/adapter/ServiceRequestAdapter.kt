package com.example.projet_integration.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_integration.R
import com.example.projet_integration.models.ServiceRequest
import com.example.projet_integration.ChatActivity // Import your ChatActivity

class ServiceRequestAdapter(
    private val context: Context,
    private val serviceRequests: List<ServiceRequest>
) : RecyclerView.Adapter<ServiceRequestAdapter.ServiceRequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceRequestViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_service_request, parent, false)
        return ServiceRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceRequestViewHolder, position: Int) {
        val serviceRequest = serviceRequests[position]
        holder.status.text = serviceRequest.status
        holder.description.text = serviceRequest.description
        holder.paymentStatus.text = serviceRequest.paymentStatus
        holder.clientId.text = "Client ID: ${serviceRequest.clientId}"

        // Set an icon for the status
        when (serviceRequest.status) {
            "Completed" -> holder.icon.setImageResource(R.drawable.ic_completed)
            "In Progress" -> holder.icon.setImageResource(R.drawable.ic_progress)
            else -> holder.icon.setImageResource(R.drawable.ic_pending)
        }

        // Handle the "Contact" button click
        holder.buttonContact.setOnClickListener {
            val clientId = serviceRequest.clientId // Get clientId from serviceRequest
            val freelancerId = getClientId(context) // Get the current freelancer's ID from the context (freelancer account)

            Log.d("ServiceRequestAdapter", "clientId: $clientId, freelancerId: $freelancerId") // Log for debugging

            if (clientId != null && freelancerId != null) {
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("CLIENT_ID", clientId) // Pass the client ID
                intent.putExtra("FREELANCER_ID", freelancerId) // Pass the freelancer ID
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Client ID or Freelancer ID is missing", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = serviceRequests.size

    inner class ServiceRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.icon_status)
        val status: TextView = itemView.findViewById(R.id.status)
        val description: TextView = itemView.findViewById(R.id.description)
        val paymentStatus: TextView = itemView.findViewById(R.id.payment_status)
        val clientId: TextView = itemView.findViewById(R.id.client_id)
        val buttonContact: Button = itemView.findViewById(R.id.button_contact) // Reference to the Contact button
    }

    // Function to get the clientId from shared preferences (assuming it's stored there)
    private fun getClientId(context: Context): String? {
        val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("CLIENT_ID", null)
    }
}
