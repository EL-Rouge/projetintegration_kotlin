package com.example.projet_integration

import com.example.projet_integration.models.LoginRequest
import com.example.projet_integration.models.LoginResponse
import com.example.projet_integration.models.ServiceRequest
import com.example.projet_integration.models.User
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    // User registration
    @POST("/api/users/register")
    fun registerUser(@Body user: User): Call<User>

    // User login
    @POST("/api/users/login")
    fun loginUser(@Body loginData: LoginRequest): Call<LoginResponse>

    // Get all service requests
    @GET("/api/service-requests")
    fun getAllServices(): Call<List<ServiceRequest>>

    // Get a service request by ID
    @GET("/api/service-requests/{id}")
    fun getServiceById(@Path("id") id: Int): Call<ServiceRequest>

    // Create a new service request
    @POST("/api/service-requests")
    fun createService(@Body serviceRequest: ServiceRequest): Call<ServiceRequest>

}
