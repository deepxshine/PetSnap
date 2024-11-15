package com.example.petsnap.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.petsnap.databinding.RvCommentSectionBinding
import com.example.petsnap.domain.model.CommentsResponse

class CommentAdapter (

) :  PagingDataAdapter<CommentsResponse, CommentAdapter.CommentViewHolder>(COMMENT_COMPARATOR)
{
    companion object {
        private val COMMENT_COMPARATOR = object : DiffUtil.ItemCallback<CommentsResponse>() {
            override fun areItemsTheSame(
                oldItem: CommentsResponse,
                newItem: CommentsResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CommentsResponse,
                newItem: CommentsResponse
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class CommentViewHolder(private val binding: RvCommentSectionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: CommentsResponse) {
            binding.apply {
                commentUsername.text = comment.username
                commentText.text = comment.comment

                if (comment.commentedByUser) {
                    commentText.setOnClickListener {
                        //todo: choose to delete comment
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvCommentSectionBinding.inflate(inflater, parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = getItem(position)
        comment?.let {
            holder.bind(it)
        }
    }
}