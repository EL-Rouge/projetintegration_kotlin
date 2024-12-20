package com.example.projet_integration

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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

        // Retrieve IDs from Intent
        val clientId = intent.getStringExtra("CLIENT_ID")
        val freelancerId = intent.getStringExtra("FREELANCER_ID")
        val serviceId = intent.getStringExtra("SERVICE_ID")

        Log.d("ChatActivity", "Intent Data - CLIENT_ID: $clientId, FREELANCER_ID: $freelancerId, SERVICE_ID: $serviceId")

        if (clientId == null || freelancerId == null || serviceId == null) {
            Log.e("ChatActivity", "Missing clientId, freelancerId, or serviceId")
            Toast.makeText(this, "Missing clientId, freelancerId, or serviceId", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Initialize RecyclerView with ChatAdapter
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatAdapter = ChatAdapter(this, chatMessages, clientId) // Pass clientId as currentUserId
        chatRecyclerView.adapter = chatAdapter

        // Load chat history
        loadChat(serviceId, freelancerId, clientId)

        // Send message on button click
        sendButton.setOnClickListener {
            val messageContent = messageInput.text.toString()
            if (messageContent.isNotBlank()) {
                sendMessage(freelancerId, clientId, serviceId, messageContent)
                messageInput.text.clear()  // Clear the input after sending the message
            } else {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadChat(serviceId: String, freelancerId: String, clientId: String) {
        Log.d("ChatActivity", "Loading chat with serviceId: $serviceId, freelancerId: $freelancerId, clientId: $clientId")

        val call = RetrofitInstance.apiService.getChat(serviceId, clientId, freelancerId)
        call.enqueue(object : Callback<List<Chat>> {
            override fun onResponse(call: Call<List<Chat>>, response: Response<List<Chat>>) {
                if (response.isSuccessful) {
                    response.body()?.let { chats ->
                        Log.d("ChatActivity", "Chat loaded successfully: $chats")
                        chatMessages.clear()

                        // Flatten all messages from all chats and add to the list
                        chatMessages.addAll(chats.flatMap { it.messages })

                        // Notify adapter that data has changed
                        chatAdapter.notifyDataSetChanged()

                        // Scroll to the latest message after updating the adapter
                        chatRecyclerView.scrollToPosition(chatMessages.size - 1)
                    } ?: run {
                        Log.e("ChatActivity", "Response body is null")
                    }
                } else {
                    Log.e("ChatActivity", "Failed to load chat: ${response.errorBody()?.string()}")
                    Toast.makeText(this@ChatActivity, "Failed to load chat", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Chat>>, t: Throwable) {
                Log.e("ChatActivity", "Error while loading chat: ${t.message}")
                Toast.makeText(this@ChatActivity, "Error while loading chat", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendMessage(freelancerId: String, clientId: String, serviceId: String, messageContent: String) {
        val message = Message(messageContent, freelancerId) // The sender is the freelancer
        val messages = listOf(message)

        val chatRequest = ChatRequest(
            freelancerId = freelancerId,
            clientId = clientId, // The clientId is fetched from the service
            serviceId = serviceId, // Pass the serviceId
            messages = messages
        )

        val call = RetrofitInstance.apiService.createChat(chatRequest)
        call.enqueue(object : Callback<Chat> {
            override fun onResponse(call: Call<Chat>, response: Response<Chat>) {
                if (response.isSuccessful) {
                    response.body()?.let { chat ->
                        Log.d("ChatActivity", "Chat created: $chat")
                        chatMessages.addAll(chat.messages)  // Add new messages to the existing list
                        chatAdapter.notifyItemInserted(chatMessages.size - 1)  // Notify that a new item was inserted

                        // Scroll to the latest message after insertion
                        chatRecyclerView.scrollToPosition(chatMessages.size - 1)
                    }
                } else {
                    Log.e("ChatActivity", "Failed to create chat: ${response.errorBody()?.string()}")
                    Toast.makeText(this@ChatActivity, "Failed to send message", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Chat>, t: Throwable) {
                Log.e("ChatActivity", "Error creating chat: ${t.message}")
                Toast.makeText(this@ChatActivity, "Error while sending message", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
