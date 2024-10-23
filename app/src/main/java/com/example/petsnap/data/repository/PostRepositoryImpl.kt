package com.example.petsnap.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.petsnap.data.remote.PostService
import com.example.petsnap.domain.model.PostsOnMainPageResponse
import com.example.petsnap.domain.repository.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(private val postService: PostService) : PostRepository {

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


}