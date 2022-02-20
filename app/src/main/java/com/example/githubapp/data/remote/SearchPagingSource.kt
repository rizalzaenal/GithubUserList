package com.example.githubapp.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubapp.data.model.UserItem
import com.example.githubapp.data.util.calculateNextPage

class SearchPagingSource(private val query: String, private val githubService: GithubService): PagingSource<Int, UserItem>() {
    override fun getRefreshKey(state: PagingState<Int, UserItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserItem> {
        return try {
            val pageNumber = params.key ?: 1
            Log.d("SearchPagingSource", " inside")
            val response = githubService.searchUser(query, pageNumber, params.loadSize)
            Log.d("SearchPagingSource", response.toString())
            val nextPageKey = calculateNextPage(pageNumber, response.totalCount ?: 0, params.loadSize)
            LoadResult.Page(response.userItems ?: listOf(), null, nextPageKey)
        } catch (e: Exception) {
            Log.d("SearchPagingSource", " exception")
            LoadResult.Error(e)
        }
    }
}