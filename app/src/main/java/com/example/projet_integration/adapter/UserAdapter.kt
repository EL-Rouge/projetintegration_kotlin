package com.example.projet_integration.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_integration.R
import com.example.projet_integration.models.User

class UserAdapter(
    private val onEditUser: (User) -> Unit,
    private val onDeleteUser: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var users: List<User> = listOf()

    fun submitList(userList: List<User>) {
        users = userList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = users.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userNameTextView: TextView = itemView.findViewById(R.id.userName)
        private val userEmailTextView: TextView = itemView.findViewById(R.id.userEmail)
        private val editButton: Button = itemView.findViewById(R.id.editButton)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        fun bind(user: User) {
            userNameTextView.text = user.username
            userEmailTextView.text = user.email

            editButton.setOnClickListener {
                onEditUser(user)
            }

            deleteButton.setOnClickListener {
                onDeleteUser(user)
            }
        }
    }
}
