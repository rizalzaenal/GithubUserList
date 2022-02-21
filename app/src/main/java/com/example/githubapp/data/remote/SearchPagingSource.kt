package com.example.githubapp.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubapp.data.model.UserItem
import com.example.githubapp.data.util.calculateNextPage
import retrofit2.HttpException

class SearchPagingSource(private val query: String, private val githubService: GithubService) :
    PagingSource<Int, UserItem>() {
    override fun getRefreshKey(state: PagingState<Int, UserItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserItem> {
        return try {
            val pageNumber = params.key ?: 1
            val response = githubService.searchUser(query, pageNumber)
            if (response.isSuccessful) {
                val nextPageKey = calculateNextPage(pageNumber, response.body()?.totalCount ?: 0, params.loadSize)
                LoadResult.Page(response.body()?.userItems ?: listOf(), null, nextPageKey)
            }else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}