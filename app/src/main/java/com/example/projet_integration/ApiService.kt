package com.example.projet_integration

import com.example.projet_integration.models.Chat
import com.example.projet_integration.models.ChatRequest
import com.example.projet_integration.models.LoginRequest
import com.example.projet_integration.models.LoginResponse
import com.example.projet_integration.models.ServiceRequest
import com.example.projet_integration.models.User
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    // Admin login
    @POST("/api/admin/login")
    fun loginAdmin(@Body loginRequest: LoginRequest): Call<LoginResponse>


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

    @GET("/api/chat")
    fun getChat(
        @Query("clientId") clientId: String,
        @Query("freelancerId") freelancerId: String
    ): Call<Chat>


    @POST("/api/chats")
    fun createChat(@Body chatRequest: ChatRequest): Call<Chat>

    @GET("/api/admin/users")
    fun getAllUsers(): Call<List<User>>

    @GET("/api/users/{id}")
    fun getUserById(@Path("id") userId: String): Call<User>

    @POST("/api/admin/users")
    fun createUser(user: User): Call<User>

    @PUT("/api/admin/users/{id}")
    fun updateUser(@Path("id") userId: String, @Body user: User): Call<User>


    @DELETE("/api/admin/users/{id}")
    fun deleteUser(@Path("id") userId: String): Call<Void>

    @GET("/api/admin/service-requests")
    fun getAllServiceRequests(): Call<List<ServiceRequest>>



    @PUT("/api/admin/service-requests/{id}")
    fun updateServiceRequest(@Path("id") id: String, @Body serviceRequest: ServiceRequest): Call<Void>




    @DELETE("/api/admin/service-requests/{id}")
    fun deleteServiceRequest(@Path("id") id: Int): Call<Void>

}

