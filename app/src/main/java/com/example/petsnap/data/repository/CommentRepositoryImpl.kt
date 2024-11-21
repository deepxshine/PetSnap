package com.example.petsnap.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.petsnap.data.remote.CommentService
import com.example.petsnap.domain.model.CommentsRequest
import com.example.petsnap.domain.model.CommentsResponse
import com.example.petsnap.domain.model.ResponseMsg
import com.example.petsnap.domain.repository.CommentRepository
import com.example.petsnap.utils.Resource
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(private val commentService: CommentService) : CommentRepository {
    override suspend fun getPostComments(commentsRequest: CommentsRequest): PagingSource<Int, CommentsResponse> {
        return object : PagingSource<Int, CommentsResponse>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommentsResponse> {
                return try {
                    val page = params.key ?: 0 // key 是当前页码，如果 params.key 为 null，则默认从第0页开始。
                    val response = commentService.getPostComments(commentsRequest.postId, commentsRequest.userId, page, params.loadSize)
                    println(response)
                    LoadResult.Page(
                        data = response.content,
                        prevKey = if (page == 0) null else page - 1,
                        nextKey = if (response.content.isEmpty()) null else page + 1
                    )
                } catch (e: Exception) {
                    LoadResult.Error(e)
                }
            }

            //getRefreshKey 方法用于在刷新数据时确定从哪个键（页码）开始加载数据
            override fun getRefreshKey(state: PagingState<Int, CommentsResponse>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                        ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
                }
            }
        }
    }

    override suspend fun addComment(userId: Long, postId: Long, comment: String): Resource<CommentsResponse> {
        return try {
            val commentText = comment.toRequestBody()
            val response = commentService.addComment(userId, postId, commentText)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Resource.error(response.message(), null)
            }
        } catch (e: Exception) {
            Resource.error(e.message ?: "Unknown error", null)
        }
    }

    override suspend fun removeComment(userId: Long, commentId: Long): Resource<ResponseMsg> {
        return try {
            val response = commentService.removeComment(userId, commentId)
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Resource.error(response.message(), null)
            }
        } catch (e: Exception) {
            Resource.error(e.message ?: "Unknown error", null)
        }
    }


}