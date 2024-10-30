package com.example.petsnap.ui.friends

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petsnap.R
import com.example.petsnap.databinding.RvFragmentFriendsBinding
import com.example.petsnap.domain.model.FriendshipResponse

class FollowersAdapter(
    private val viewModel: FriendsViewModel
) : ListAdapter<FriendshipResponse, FollowersAdapter.FollowerViewHolder>(FRIENDSHIP_COMPARATOR) {

    companion object {
        private val FRIENDSHIP_COMPARATOR = object : DiffUtil.ItemCallback<FriendshipResponse>() {
            override fun areItemsTheSame(
                oldItem: FriendshipResponse,
                newItem: FriendshipResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: FriendshipResponse,
                newItem: FriendshipResponse
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class FollowerViewHolder(private val binding: RvFragmentFriendsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friendship: FriendshipResponse) {

            binding.apply {
                // загрузка аватара
                Glide.with(itemView.context)
                    .load(friendship.avatar)
                    .error(R.mipmap.ic_launcher)
                    .into(friendsAvatar)

                // загрузка других данных
                friendsUsername.text = friendship.username
                subscribeToggleButton.isChecked = friendship.followedByUser

                subscribeToggleButton.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) { // если подписывается
                        viewModel.followUser(friendship.id)

                    } else { // если отписывается
                         viewModel.unfollowUser(friendship.id)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvFragmentFriendsBinding.inflate(inflater, parent, false)
        return FollowerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        val friendship = getItem(position)
        friendship?.let {
            holder.bind(it)
        }
    }
}
