package com.example.projet_integration.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_integration.R
import com.example.projet_integration.models.ServiceRequest

class ServiceRequestAdapter(private val context: Context, private val serviceRequests: List<ServiceRequest>) :
    RecyclerView.Adapter<ServiceRequestAdapter.ServiceRequestViewHolder>() {

    // Create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceRequestViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_service_request, parent, false)
        return ServiceRequestViewHolder(view)
    }

    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: ServiceRequestViewHolder, position: Int) {
        val serviceRequest = serviceRequests[position]
        holder.status.text = serviceRequest.status
        holder.description.text = serviceRequest.description
        holder.paymentStatus.text = serviceRequest.paymentStatus
        holder.clientId.text = "Client ID: ${serviceRequest.clientId}"

//         You can set an icon for the status (e.g., pending, completed, etc.)
         if (serviceRequest.status == "Completed") {
            holder.icon.setImageResource(R.drawable.ic_completed)
         } else if (serviceRequest.status == "In Progress") {
            holder.icon.setImageResource(R.drawable.ic_progress)
         }else{
             holder.icon.setImageResource(R.drawable.ic_pending)
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
    }
}
