package com.example.projet_integration.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_integration.R
import com.example.projet_integration.models.ServiceRequest

class ServiceRequestAdapter2(
    private val serviceRequests: List<ServiceRequest>,
    private val onUpdateClick: (ServiceRequest) -> Unit,
    private val onDeleteClick: (ServiceRequest) -> Unit
) : RecyclerView.Adapter<ServiceRequestAdapter2.ServiceViewHolder>() {

    inner class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextView = itemView.findViewById(R.id.description)
        val status: TextView = itemView.findViewById(R.id.status)
        val paymentStatus: TextView = itemView.findViewById(R.id.payment_status)
        val clientId: TextView = itemView.findViewById(R.id.client_id)
        val updateButton: Button = itemView.findViewById(R.id.update_button)
        val deleteButton: Button = itemView.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_service_with2button, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val serviceRequest = serviceRequests[position]
        holder.description.text = serviceRequest.description
        holder.status.text = "Status: ${serviceRequest.status}"
        holder.paymentStatus.text = "Payment: ${serviceRequest.paymentStatus}"
        holder.clientId.text = "Client ID: ${serviceRequest.clientId}"

        holder.updateButton.setOnClickListener { onUpdateClick(serviceRequest) }
        holder.deleteButton.setOnClickListener { onDeleteClick(serviceRequest) }
    }

    override fun getItemCount() = serviceRequests.size
}
