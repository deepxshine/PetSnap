package com.example.petsnap.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petsnap.R
import com.example.petsnap.databinding.RvFragmentSearchBinding
import com.example.petsnap.domain.model.UserSearchResponse

class UsersAdapter(private var users: List<UserSearchResponse>) :
    RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    inner class UserViewHolder(val binding: RvFragmentSearchBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = RvFragmentSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.binding.apply {
            usernameTextView.text = user.username
            Glide.with(avatarImageView.context)
                .load(user.avatar)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(avatarImageView)
        }
    }

    override fun getItemCount(): Int = users.size

    //обновление списка пользователей
    fun updateUsers(newUsers: List<UserSearchResponse>) {
        users = newUsers
        notifyDataSetChanged()
    }
}



