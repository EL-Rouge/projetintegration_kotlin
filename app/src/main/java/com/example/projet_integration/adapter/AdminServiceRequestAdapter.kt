package com.example.projet_integration.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_integration.R
import com.example.projet_integration.models.ServiceRequest

class AdminServiceRequestAdapter(
    private val onEditRequest: (ServiceRequest) -> Unit,
    private val onDeleteRequest: (ServiceRequest) -> Unit
) : RecyclerView.Adapter<AdminServiceRequestAdapter.ServiceRequestViewHolder>() {

    private var serviceRequests: List<ServiceRequest> = listOf()

    fun submitList(serviceRequestList: List<ServiceRequest>) {
        serviceRequests = serviceRequestList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceRequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_item_service_request, parent, false)
        return ServiceRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceRequestViewHolder, position: Int) {
        val serviceRequest = serviceRequests[position]
        holder.bind(serviceRequest)
    }

    override fun getItemCount(): Int = serviceRequests.size

    inner class ServiceRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val serviceDescriptionTextView: TextView = itemView.findViewById(R.id.serviceDescription)
        private val editButton: Button = itemView.findViewById(R.id.editButton)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        fun bind(serviceRequest: ServiceRequest) {
            serviceDescriptionTextView.text = serviceRequest.description

            editButton.setOnClickListener {
                onEditRequest(serviceRequest)
            }

            deleteButton.setOnClickListener {
                onDeleteRequest(serviceRequest)
            }
        }
    }
}
