package com.example.projet_integration.models

data class LoginResponse(
    val user: User,
    val message: String, // Message from server, e.g., "Login successful"
    val success: Boolean // True if login is successful, otherwise false

)

