package com.example.projet_integration

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_integration.adapter.ChatAdapter
import com.example.projet_integration.models.Chat
import com.example.projet_integration.models.ChatRequest
import com.example.projet_integration.models.Message
import com.example.projet_integration.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var chatAdapter: ChatAdapter
    private val chatMessages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // Hide the ActionBar here

        setContentView(R.layout.chat_file)

        // Initialize views
        chatRecyclerView = findViewById(R.id.chat_recycler_view)
        messageInput = findViewById(R.id.message_input)
        sendButton = findViewById(R.id.send_button)

        // Retrieve client ID and freelancer ID from Intent
        // Inside ChatActivity
        val clientId = intent.getStringExtra("CLIENT_ID")
        val freelancerId = intent.getStringExtra("FREELANCER_ID")

        Log.d("ChatActivity", "Received clientId: $clientId, freelancerId: $freelancerId") // Log for debugging

        if (clientId == null || freelancerId == null) {
            Log.e("ChatActivity", "Missing clientId or freelancerId")
            Toast.makeText(this, "Missing clientId or freelancerId", Toast.LENGTH_SHORT).show()
            finish() // Close the activity if data is missing
            return
        }


        // Initialize RecyclerView with ChatAdapter
        chatAdapter = ChatAdapter(this, chatMessages, clientId) // Pass clientId as currentUserId
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = chatAdapter

        // Load chat history
        loadChat(freelancerId, clientId)

        // Send message on button click
        sendButton.setOnClickListener {
            val messageContent = messageInput.text.toString()
            if (messageContent.isNotBlank()) {
                sendMessage(freelancerId, clientId, messageContent)
            } else {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadChat(freelancerId: String, clientId: String) {
        val call = RetrofitInstance.apiService.getChat(freelancerId, clientId)
        call.enqueue(object : Callback<Chat> {
            override fun onResponse(call: Call<Chat>, response: Response<Chat>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        chatMessages.clear()
                        chatMessages.addAll(it.messages)
                        chatAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e("ChatActivity", "Failed to load chat: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Chat>, t: Throwable) {
                Log.e("ChatActivity", "Error: ${t.message}")
            }
        })
    }

    private fun sendMessage(freelancerId: String, clientId: String, content: String) {
        val message = Message(content, clientId)
        val chatRequest = ChatRequest(freelancerId, clientId, listOf(message))

        // Disable send button temporarily
        sendButton.isEnabled = false

        val call = RetrofitInstance.apiService.createChat(chatRequest)
        call.enqueue(object : Callback<Chat> {
            override fun onResponse(call: Call<Chat>, response: Response<Chat>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        chatMessages.add(it.messages.last()) // Add the new message to the list
                        chatAdapter.notifyItemInserted(chatMessages.size - 1)
                        messageInput.text.clear()
                    }
                } else {
                    Log.e("ChatActivity", "Failed to send message: ${response.errorBody()?.string()}")
                }

                sendButton.isEnabled = true // Re-enable send button
            }

            override fun onFailure(call: Call<Chat>, t: Throwable) {
                Log.e("ChatActivity", "Error: ${t.message}")
                sendButton.isEnabled = true // Re-enable send button
            }
        })
    }
}
