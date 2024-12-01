package com.example.projet_integration

import com.example.projet_integration.models.ServiceRequest
import com.example.projet_integration.models.User
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @GET("/users")
    fun getAllUsers(): Call<List<User>>

    @GET("/users/{id}")
    fun getUserById(@Path("id") id: Long): Call<User>

    @POST("/users")
    fun createUser(@Body user: User): Call<User>

    @PUT("/users/{id}")
    fun updateUser(@Path("id") id: Long, @Body user: User): Call<User>

    @DELETE("/users/{id}")
    fun deleteUser(@Path("id") id: Long): Call<Void>

    @POST("/users/login")
    fun loginUser(@Body user: User): Call<User>

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
