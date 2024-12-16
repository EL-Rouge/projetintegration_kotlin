    package com.example.projet_integration.models

    data class User(
        val username: String,
        val password: String,
        val email: String,
        val accountType: String // e.g., "client" or "freelancer"
    )
