package com.example.projet_integration.models

data class LoginResponse(
    val success: Boolean, // Indicating login success
    val message: String?  // Optional: A message from the server
)
