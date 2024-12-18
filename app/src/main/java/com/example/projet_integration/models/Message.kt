package com.example.projet_integration.models

data class Message(
    val content: String,
    val sender: String,
    val _id: String? = null, // Optional, as it will be returned by the server
    val timestamp: String? = null // Optional, as it will be returned by the server
)
