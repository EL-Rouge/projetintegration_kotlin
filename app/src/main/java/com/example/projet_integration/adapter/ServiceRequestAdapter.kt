package com.example.projet_integration.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_integration.ChatActivity
import com.example.projet_integration.MainActivity
import com.example.projet_integration.R
import com.example.projet_integration.models.ServiceRequest

class ServiceRequestAdapter(
    private val activity: Activity, // Changed context to Activity
    private val serviceRequests: List<ServiceRequest>
) : RecyclerView.Adapter<ServiceRequestAdapter.ServiceRequestViewHolder>() {

    // Create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceRequestViewHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.item_service_request_with_contact, parent, false)
        return ServiceRequestViewHolder(view)
    }

    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: ServiceRequestViewHolder, position: Int) {
        val serviceRequest = serviceRequests[position]
        holder.status.text = serviceRequest.status
        holder.description.text = serviceRequest.description
        holder.paymentStatus.text = serviceRequest.paymentStatus
        holder.clientId.text = "Client ID: ${serviceRequest.clientId}"

        // You can set an icon for the status (e.g., pending, completed, etc.)
        if (serviceRequest.status == "Completed") {
            holder.icon.setImageResource(R.drawable.ic_completed)
        } else if (serviceRequest.status == "In Progress") {
            holder.icon.setImageResource(R.drawable.ic_progress)
        } else {
            holder.icon.setImageResource(R.drawable.ic_pending)
        }

        // Set the OnClickListener for the Contact button
        holder.contactButton.setOnClickListener {
            // Ensure you have the freelancerId and clientId
            val freelancerId = serviceRequest.freelancerId
            val clientId = serviceRequest.clientId

            // Create the intent to navigate to the ChatActivity
            val intent = Intent(activity, ChatActivity::class.java).apply {
                putExtra("freelancerId", freelancerId)
                putExtra("clientId", clientId)
            }

            // Start the chat activity
            activity.startActivity(intent)
        }

    }

    // Return the number of items
    override fun getItemCount(): Int = serviceRequests.size

    // ViewHolder to hold references to the views
    inner class ServiceRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.icon_status)
        val status: TextView = itemView.findViewById(R.id.status)
        val description: TextView = itemView.findViewById(R.id.description)
        val paymentStatus: TextView = itemView.findViewById(R.id.payment_status)
        val clientId: TextView = itemView.findViewById(R.id.client_id)
        val contactButton: Button = itemView.findViewById(R.id.contact_button)
    }
}
