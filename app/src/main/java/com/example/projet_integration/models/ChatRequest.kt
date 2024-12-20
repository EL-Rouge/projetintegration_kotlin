package com.example.projet_integration.models

data class ChatRequest(
    val freelancerId: String,
    val clientId: String,
    val serviceId: String, // Add this field
    val messages: List<Message>
)

