package com.example.petsnap.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petsnap.R
import com.example.petsnap.databinding.RvFragmentHomeBinding
import com.example.petsnap.domain.model.PostsOnMainPageResponse

class HomeAdapter : PagingDataAdapter<PostsOnMainPageResponse, HomeAdapter.HomeViewHolder>(POST_COMPARATOR) {


    companion object {
        private val POST_COMPARATOR = object : DiffUtil.ItemCallback<PostsOnMainPageResponse>() {
            override fun areItemsTheSame(oldItem: PostsOnMainPageResponse, newItem: PostsOnMainPageResponse): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PostsOnMainPageResponse, newItem: PostsOnMainPageResponse): Boolean {
                return oldItem == newItem
            }
        }
    }


    inner class HomeViewHolder(private val binding: RvFragmentHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostsOnMainPageResponse) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(post.user.avatar)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(postAvatar)

                postUsername.text = post.user.username
                postText.text = post.text

                Glide.with(itemView.context)
                    .load(post.image)
                    .error(R.drawable.ic_dashboard_black_24dp)
                    .into(postImageView)

                // set like color
                val likeColor = if (post.likedByUser) {
                    ContextCompat.getColor(itemView.context, R.color.red)
                } else {
                    ContextCompat.getColor(itemView.context, R.color.black)
                }

                postLike.setColorFilter(likeColor)

                likesCount.text = post.likesCount.toString()

                commentsCount.text = post.commentsCount.toString()


            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = RvFragmentHomeBinding.inflate(inflater, parent, false)
        return HomeViewHolder(binding)

    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val post = getItem(position)
        post?.let {
            holder.bind(it)
        }
    }


}