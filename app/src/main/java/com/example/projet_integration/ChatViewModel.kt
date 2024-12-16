package com.example.projet_integration

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.projet_integration.models.Chat
import com.example.projet_integration.models.MessageRequest
import com.example.projet_integration.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers

class ChatViewModel(private val context: Context) : ViewModel() {

    // Function to store chatId in SharedPreferences
    private fun storeChatId(chatId: String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("chatId", chatId) // Save the chatId under a key
        editor.apply() // Apply the changes
    }

    // Function to get the stored chatId from SharedPreferences
    private fun getStoredChatId(): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("chatId", null) // Return the stored chatId, or null if not found
    }

    // Create a new chat (Chat with freelancer and client)
    fun createChat(freelancerId: String, clientId: String) = liveData(Dispatchers.IO) {
        try {
            val chat = Chat(freelancerId = freelancerId, clientId = clientId)
            val response = RetrofitInstance.apiService.createChat(chat)

            if (response.isSuccessful) {
                val createdChat = response.body()
                createdChat?.let {
                    // Store chatId in SharedPreferences
                    it.id?.let { chatId ->
                        storeChatId(chatId) // Only store if chatId is not null
                    }
                    emit(createdChat)
                }
            } else {
                emit(null)
            }
        } catch (e: Exception) {
            emit(null)
        }
    }

    // Send message to the existing chat
    fun sendMessage(messageRequest: MessageRequest) = liveData(Dispatchers.IO) {
        val chatId = getStoredChatId() // Get the stored chatId

        if (!chatId.isNullOrEmpty()) { // Ensure chatId is not null or empty
            try {
                val response = RetrofitInstance.apiService.addMessage(chatId, messageRequest)

                if (response.isSuccessful) {
                    emit(response.body())
                } else {
                    emit(null)
                }
            } catch (e: Exception) {
                emit(null)
            }
        } else {
            emit(null) // If no chatId is stored, emit null
        }
    }
}
