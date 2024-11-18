package com.example.petsnap.ui.home

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.petsnap.databinding.DeleteCommentDialogBinding
import com.example.petsnap.databinding.RvCommentSectionBinding
import com.example.petsnap.domain.model.CommentsResponse

class CommentAdapter(
    private val commentsViewModel: CommentsViewModel,
    private val onCommentDeleted: (postId: Long) -> Unit
) : PagingDataAdapter<CommentsResponse, CommentAdapter.CommentViewHolder>(COMMENT_COMPARATOR) {
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

    inner class CommentViewHolder(private val binding: RvCommentSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(comment: CommentsResponse) {

            // создать окно диалога для удаления поста
            val deleteDialog = Dialog(itemView.context)
            val deletebinding: DeleteCommentDialogBinding = DeleteCommentDialogBinding.inflate(
                LayoutInflater.from(itemView.context),
                deleteDialog.findViewById(android.R.id.content),
                false
            )
            deleteDialog.setContentView(deletebinding.root)

            // получить userId
            val sharedPreferences =
                itemView.context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getLong("user_id", -1L)

            binding.apply {

                commentUsername.text = comment.username
                commentText.text = comment.comment

                if (comment.commentedByUser) {

                    // появление окна для удаления комментария
                    commentText.setOnClickListener {

                        deleteDialog.show()

                        // удаление комментария
                        deletebinding.buttonDeleteComment.setOnClickListener {

                            commentsViewModel.removeComment(userId, comment.id, comment.postId)

                            deleteDialog.hide()

                            // сообщить homeAdapter об удалении комментария
                            onCommentDeleted(comment.postId)

                            notifyItemRemoved(bindingAdapterPosition)

                        }

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