package com.example.petsnap.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.petsnap.data.remote.PostService
import com.example.petsnap.domain.model.CreatePostRequest
import com.example.petsnap.domain.model.PostsOnMainPageResponse
import com.example.petsnap.domain.repository.PostRepository
import com.example.petsnap.utils.Resource
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(private val postService: PostService) :
    PostRepository {

    override fun getPagingSource(userId: Long): PagingSource<Int, PostsOnMainPageResponse> {
        return object : PagingSource<Int, PostsOnMainPageResponse>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostsOnMainPageResponse> {
                return try {
                    val page = params.key ?: 0
                    val response = postService.getAllPosts(userId, page, params.loadSize)

                    LoadResult.Page(
                        data = response.content,
                        prevKey = if (page == 0) null else page - 1,
                        nextKey = if (response.content.isEmpty()) null else page + 1
                    )
                } catch (e: Exception) {
                    LoadResult.Error(e)
                }
            }

            override fun getRefreshKey(state: PagingState<Int, PostsOnMainPageResponse>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                        ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
                }
            }
        }
    }

    override suspend fun createPost(
        createPostRequest: CreatePostRequest
    ): Resource<PostsOnMainPageResponse> {

        return try {
            val filePart = createPostRequest.toFilePart()
            val textPart = createPostRequest.textToPart()

            val response = postService.createPost(
                filePart,
                textPart,
                createPostRequest.userId
            )

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
