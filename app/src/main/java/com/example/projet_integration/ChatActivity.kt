package com.example.projet_integration

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.projet_integration.models.MessageRequest
import com.example.projet_integration.R

class ChatActivity : AppCompatActivity() {

    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val freelancerId = "freelancer_id_here" // Example freelancer ID
        val clientId = "client_id_here" // Example client ID

        // Create chat
        chatViewModel.createChat(freelancerId, clientId).observe(this, Observer { chat ->
            chat?.let {
                // Chat created successfully, now you can send messages
                val messageRequest = MessageRequest("Hello, freelancer!")
                chatViewModel.sendMessage(messageRequest).observe(this, Observer { response ->
                    // Handle the message response
                    response?.let {
                        // Message sent successfully
                    } ?: run {
                        // Handle error (message not sent)
                    }
                })
            } ?: run {
                // Handle error (chat not created)
            }
        })
    }
}
