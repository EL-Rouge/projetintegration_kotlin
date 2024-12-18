package com.example.projet_integration.models

data class Chat(
    val messages: List<Message>,
    val freelancerId: String,
    val clientId: String,
    val status: String? = "Pending", // Optional, defaults to "Pending"
    val _id: String? = null, // Optional, as it will be returned by the server
    val createdAt: String? = null, // Optional, returned by the server
    val updatedAt: String? = null, // Optional, returned by the server
    val __v: Int? = null // Optional, returned by the server
)
