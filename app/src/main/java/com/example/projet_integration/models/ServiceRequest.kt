package com.example.projet_integration.models
data class ServiceRequest(
    val description: String,
    val status: String = "Pending",  // Default value for status
    val paymentStatus: String = "Unpaid",  // Default value for paymentStatus
    val createdAt: String = "",
    val updatedAt: String = "",
    val clientId: String?, // Nullable because it might be null
    val freelancerId: String?=null
)




