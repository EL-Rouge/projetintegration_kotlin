package com.example.projet_integration

import com.example.projet_integration.models.Chat
import com.example.projet_integration.models.LoginRequest
import com.example.projet_integration.models.LoginResponse
import com.example.projet_integration.models.MessageRequest
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
    fun getServicesByClientId(@Query("clientId") clientId: String): Call<List<ServiceRequest>>


    // Get a service request by ID
    @GET("/api/service-requests/{id}")
    fun getServiceById(@Path("id") id: String): Call<ServiceRequest>

    // Create a new service request
    @POST("/api/service-requests")
    fun createService(@Body serviceRequest: ServiceRequest): Call<ServiceRequest>

    // Create a new chat
    @POST("api/chats")
    suspend fun createChat(@Body chat: Chat): Response<Chat>

    @POST("api/chats/{chatId}/add-message")
    suspend fun addMessage(@Path("chatId") chatId: String, @Body messageRequest: MessageRequest): Response<Chat>


    // Get a chat by its ID
    @GET("/api/chats/{id}")
    suspend fun getChatById(@Path("id") id: String): Response<Chat>
}
