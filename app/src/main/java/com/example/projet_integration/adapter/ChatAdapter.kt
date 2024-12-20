package com.example.projet_integration.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_integration.R
import com.example.projet_integration.models.Message
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(
    private val context: Context,
    private val messages: MutableList<Message>, // Use mutableList for easy updating
    private val currentUserId: String
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    // Determine whether the message is sent or received
    override fun getItemViewType(position: Int): Int {
        return if (messages[position].sender == currentUserId) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    // Inflate different layouts based on the message type (sent or received)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layout = if (viewType == VIEW_TYPE_SENT) {
            R.layout.item_message_sent
        } else {
            R.layout.item_message_received
        }
        val view = LayoutInflater.from(context).inflate(layout, parent, false)
        return ChatViewHolder(view)
    }

    // Bind message content, sender's name, and timestamp to the views
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        holder.messageContent.text = message.content
        holder.messageTimestamp.text = formatTimestamp(message.timestamp)

        // Set freelancer's name for received messages
        if (message.sender != currentUserId) {
            holder.messageSender.text = "Freelancer" // Set sender's name
            holder.messageSender.visibility = View.VISIBLE
        } else {
            holder.messageSender.visibility = View.GONE // Hide for sent messages
        }
    }

    // Return the total number of messages
    override fun getItemCount(): Int = messages.size

    // ViewHolder for the chat messages
    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageContent: TextView = itemView.findViewById(R.id.message_content)
        val messageTimestamp: TextView = itemView.findViewById(R.id.message_timestamp)
        val messageSender: TextView = itemView.findViewById(R.id.message_sender)  // TextView for sender's name
    }

    // Function to format timestamp into a more user-friendly format
    private fun formatTimestamp(timestamp: String?): String {
        if (timestamp == null) return ""
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return try {
            val date = inputFormat.parse(timestamp)
            date?.let { outputFormat.format(it) } ?: ""
        } catch (e: Exception) {
            ""
        }
    }
}


