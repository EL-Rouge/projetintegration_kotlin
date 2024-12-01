package com.example.projet_integration.network

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody

class MockInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val responseString = when {
            request.url.encodedPath == "/users" && request.method == "GET" -> FakeData.usersList
            request.url.encodedPath == "/users/1" && request.method == "GET" -> FakeData.singleUser
            request.url.encodedPath == "/users" && request.method == "POST" -> """
                {"id": 5, "username": "newuser", "password": "newpass", "email": "newuser@example.com", "accounttype": "client"}
            """
            request.url.encodedPath.startsWith("/users/") && request.method == "PUT" -> """
                {"id": ${request.url.pathSegments.last()}, "username": "updatedUser", "password": "updatedPass", "email": "updated@example.com", "accounttype": "client"}
            """
            request.url.encodedPath.startsWith("/users/") && request.method == "DELETE" -> """
                {}
            """

            // New logic for ServiceRequest
            request.url.encodedPath == "/services" && request.method == "GET" -> FakeData.serviceRequestList
            request.url.encodedPath == "/services/1" && request.method == "GET" -> FakeData.singleServiceRequest
            request.url.encodedPath == "/services" && request.method == "POST" -> """
                {"requestId": 3, "clientId": 1, "freelancerId": 103, "description": "New Service", "status": "Pending", "paymentStatus": "Unpaid"}
            """
            else -> "{}"
        }

        return chain.proceed(request)
            .newBuilder()
            .code(200) // HTTP OK
            .message("Mock response")
            .body(responseString.toResponseBody("application/json".toMediaType()))
            .build()
    }
}
