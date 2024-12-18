package com.example.projet_integration.models

data class ChatRequest(
    val freelancerId: String,
    val clientId: String,
    val messages: List<Message>
)
