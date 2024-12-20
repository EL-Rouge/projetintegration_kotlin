package com.example.projet_integration.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServiceRequest(
    @SerializedName("_id") val id: String,  // Required field for MongoDB
    val description: String,
    val status: String = "Pending",  // Default value for status
    val paymentStatus: String = "Unpaid",  // Default value for paymentStatus
    val createdAt: String = "",
    val updatedAt: String = "",
    val clientId: String?, // Nullable because it might be null
    val freelancerId: String? = null
) : Parcelable
