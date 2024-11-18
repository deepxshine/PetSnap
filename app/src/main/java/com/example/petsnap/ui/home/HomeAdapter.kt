package com.example.petsnap.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petsnap.R
import com.example.petsnap.databinding.RvFragmentHomeBinding
import com.example.petsnap.domain.model.PostsOnMainPageResponse
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeAdapter(
    private val viewModel: HomeViewModel,
    private val commentsViewModel: CommentsViewModel,
    private val lifecycleOwner: HomeFragment
) :
    PagingDataAdapter<PostsOnMainPageResponse, HomeAdapter.HomeViewHolder>(POST_COMPARATOR) {

    companion object {
        private val POST_COMPARATOR = object : DiffUtil.ItemCallback<PostsOnMainPageResponse>() {
            override fun areItemsTheSame(
                oldItem: PostsOnMainPageResponse,
                newItem: PostsOnMainPageResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PostsOnMainPageResponse,
                newItem: PostsOnMainPageResponse
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    private val onCommentDeleted: (postId: Long) -> Unit = {postId ->

        for (i in 0..< itemCount) {
            val post = getItem(i)
            if (post?.id == postId) {
                post.let {
                    it.commentsCount--
                    notifyItemChanged(i)
                }
            }
        }
    }

    private val expandedPostIds: MutableSet<Long> = mutableSetOf() // 设置一个列表来存储哪些帖子的评论列表是展开的


    private val recycledViewPool = RecyclerView.RecycledViewPool()

    inner class HomeViewHolder(private val binding: RvFragmentHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            post: PostsOnMainPageResponse,
            lifecycleOwner: LifecycleOwner,
            isExpanded: Boolean,
            onCommentDeleted: (postId: Long) -> Unit,
        ) {
            binding.apply {

                // 每个帖子的评论应该是独立的，需要为每个帖子创建一个独立的 CommentAdapter 实例
                //val commentAdapter = CommentAdapter(commentsViewModel)
                val commentAdapter = CommentAdapter(commentsViewModel, onCommentDeleted)
                commentRvView.adapter = commentAdapter
                commentRvView.setRecycledViewPool(recycledViewPool) // 设置 RecycledViewPool, RecycledViewPool 可以确保内层 RecyclerView 的视图复用不会受到外层 RecyclerView 的影响

                Glide.with(itemView.context)
                    .load(post.user.avatar)
                    .error(R.mipmap.ic_launcher)
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

                // like listener
                val sharedPreferences =
                    itemView.context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                val userId = sharedPreferences.getLong("user_id", -1L)

                postLike.setOnClickListener {
                    viewModel.addOrRemoveLike(post.id, userId)

                    // 更新点赞状态
                    post.likedByUser = !post.likedByUser

                    val newLikeColor = if (post.likedByUser) {
                        ContextCompat.getColor(itemView.context, R.color.red)
                    } else {
                        ContextCompat.getColor(itemView.context, R.color.black)
                    }
                    postLike.setColorFilter(newLikeColor)

                    postLike.invalidate() // 局部更新点赞按钮的 UI

                    // 更新likesCount
                    if (post.likedByUser) {
                        post.likesCount++
                    } else {
                        post.likesCount--
                    }

                    likesCount.text = post.likesCount.toString()

                    likesCount.invalidate()
                }

                likesCount.text = post.likesCount.toString()

                commentsCount.text = post.commentsCount.toString()

                // comments section inflate
                commentRvView.apply {
                    layoutManager =
                        LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
                }

                // show or hide comment section
                commentRvView.visibility = if (isExpanded) View.VISIBLE else View.GONE
                addCommentText.visibility = if (isExpanded) View.VISIBLE else View.GONE
                commentSendButton.visibility = if (isExpanded) View.VISIBLE else View.GONE

                lifecycleOwner.lifecycleScope.launch {
                    // viewHolder是没有生命周期的，要使用生命周期，可以使用 CoroutineScope 或 viewLifecycleOwner.lifecycleScope
                    lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        commentsViewModel.uiState.collectLatest { commentsState ->
                            when (commentsState) {
                                is CommentsState.Initial -> {}
                                is CommentsState.Loading -> {}
                                is CommentsState.Success -> {

                                    val comments = commentsState.comments[post.id]
                                    if (comments != null) {

                                        commentAdapter.submitData(comments)

                                    }
                                }

                                is CommentsState.Error -> {
                                    Toast.makeText(
                                        itemView.context,
                                        commentsState.msg,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }
                }


                // comment listener
                postComment.setOnClickListener {


                    if (isExpanded) {
                        expandedPostIds.remove(post.id)
                        commentRvView.visibility = View.GONE
                        addCommentText.visibility = View.GONE
                        commentSendButton.visibility = View.GONE

                    } else {

                        expandedPostIds.add(post.id)

                        // load post comments
                        commentsViewModel.loadComments(post.id, userId)

                    }
                    //notify item changed
                    notifyItemChanged(bindingAdapterPosition)
                }

                // add a comment
                commentSendButton.setOnClickListener {
                    val commentTxt = addCommentText.text.toString().trimIndent()
                    if (commentTxt.isNotEmpty()) {
                        commentsViewModel.addComment(userId, post.id, commentTxt)

                        // изменить количество комментариев
                        post.commentsCount++
                        commentsCount.text = post.commentsCount.toString()

                        addCommentText.text.clear()

                        //notify item inserted
                        notifyItemChanged(bindingAdapterPosition)
                    } else {
                        Toast.makeText(itemView.context, "Comment text cannot be empty", Toast.LENGTH_LONG).show()
                    }
                }

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

        //获取帖子的评论区展开状态
        val isExpanded = expandedPostIds.contains(post?.id)

        post?.let {
            holder.bind(
                it,
                lifecycleOwner,
                isExpanded, //isExpanded - 帖子的列表是否为展开状态
                onCommentDeleted
            )
        }
    }

}