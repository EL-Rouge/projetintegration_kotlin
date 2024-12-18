package com.example.projet_integration.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_integration.R
import com.example.projet_integration.models.ServiceRequest

class ServiceAdapter2(
    private val services: List<ServiceRequest>,
    private val onUpdateClickListener: (ServiceRequest) -> Unit,
    private val onDeleteClickListener: (ServiceRequest) -> Unit
) : RecyclerView.Adapter<ServiceAdapter2.ServiceViewHolder>() {

    class ServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val status: TextView = view.findViewById(R.id.status)
        val description: TextView = view.findViewById(R.id.description)
        val paymentStatus: TextView = view.findViewById(R.id.payment_status)
        val clientId: TextView = view.findViewById(R.id.client_id)
        val updateButton: Button = view.findViewById(R.id.update_button)
        val deleteButton: Button = view.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_service_with2button, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = services[position]
        holder.status.text = service.status
        holder.description.text = service.description
        holder.paymentStatus.text = service.paymentStatus
        holder.clientId.text = service.clientId ?: "Unknown"

        // Set click listeners for Update and Delete buttons
        holder.updateButton.setOnClickListener { onUpdateClickListener(service) }
        holder.deleteButton.setOnClickListener { onDeleteClickListener(service) }
    }

    override fun getItemCount(): Int = services.size
}
