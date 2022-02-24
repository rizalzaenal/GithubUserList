package com.example.githubapp.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubapp.data.model.UserItem
import com.example.githubapp.data.util.LinkHeaderParser
import com.example.githubapp.data.util.replaceNull
import retrofit2.HttpException
import retrofit2.Response

class SearchPagingSource(private val githubService: GithubService) :
    PagingSource<String, UserItem>() {
    override fun getRefreshKey(state: PagingState<String, UserItem>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey ?: ""
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, UserItem> {
        return try {
            val currentKey = params.key ?: ""
            val response: Response<List<UserItem>> = if (currentKey.isEmpty()) {
                githubService.users()
            } else {
                githubService.users(currentKey)
            }
            if (response.isSuccessful) {
                val linkHeader = response.headers()["Link"]
                Log.d("SearchPagingSource", linkHeader.replaceNull())
                //val dummyKey = "https://api.github.com/users?since=46"
                val nextPageKey = LinkHeaderParser(linkHeader).nextUrl
                Log.d("SearchPagingSource", nextPageKey.replaceNull())
                LoadResult.Page(response.body() ?: listOf(), null, nextPageKey)
            }else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}