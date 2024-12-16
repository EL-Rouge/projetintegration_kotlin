package com.example.projet_integration.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id") val userId: String = "",
    val username: String,
    val password: String,
    val email: String,
    val accountType: String
)
