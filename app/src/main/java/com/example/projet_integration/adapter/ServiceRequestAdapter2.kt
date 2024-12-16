package com.example.projet_integration.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_integration.R
import com.example.projet_integration.models.ServiceRequest

class ServiceAdapter2(private val services: List<ServiceRequest>) :
    RecyclerView.Adapter<ServiceAdapter2.ServiceViewHolder>() {

    class ServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val status: TextView = view.findViewById(R.id.status)
        val description: TextView = view.findViewById(R.id.description)
        val paymentStatus: TextView = view.findViewById(R.id.payment_status)
        val clientId: TextView = view.findViewById(R.id.client_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_service_request, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = services[position]
        holder.status.text = service.status
        holder.description.text = service.description
        holder.paymentStatus.text = service.paymentStatus
        holder.clientId.text = service.clientId ?: "Unknown"
    }

    override fun getItemCount(): Int = services.size
}
