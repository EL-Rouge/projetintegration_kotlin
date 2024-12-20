package com.example.projet_integration

import com.example.projet_integration.models.Chat
import com.example.projet_integration.models.ChatRequest
import com.example.projet_integration.models.LoginRequest
import com.example.projet_integration.models.LoginResponse
import com.example.projet_integration.models.ServiceRequest
import com.example.projet_integration.models.User
import retrofit2.Call
import retrofit2.Response
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

    @GET("/api/service-requests")
    fun getServicesByClientId(@Query("clientId") clientId: String): Call<List<ServiceRequest>>


    // Create a new service request
    @POST("/api/service-requests")
    fun createService(@Body serviceRequest: ServiceRequest): Call<ServiceRequest>


    @DELETE("/api/service-requests/{id}")
    fun deleteServiceRequest(@Path("id") id: String): Call<Void>

    // Update a service request by ID
    @PUT("/api/service-requests/{id}")
    fun updateServiceRequest(@Path("id") id: String, @Body updates: Map<String, String>): Call<ServiceRequest>


    @GET("/api/chats")
    fun getChat(
        @Query("serviceId") serviceId: String,
        @Query("clientId") clientId: String,
        @Query("freelancerId") freelancerId: String
    ): Call<List<Chat>>  // Assuming it returns a list of chats

    @POST("/api/chats")
    fun createChat(@Body chatRequest: ChatRequest): Call<Chat>


    @PUT("/api/service-requests/{id}/export")
    suspend fun reportServiceRequest(@Path("id") id: String): Response<Unit>

    @GET("/api/users/{id}")
    suspend fun getUserById(@Path("id") id: String): Response<User>

    @GET("api/service-requests/search")
    fun searchServiceRequests(
        @Query("description") description: String
    ): Call<List<ServiceRequest>>

}

