package com.example.githubapp.data.repository

import androidx.paging.PagingSource
import com.example.githubapp.data.model.SearchUser
import com.example.githubapp.data.model.UserDetail
import com.example.githubapp.data.model.UserItem
import com.example.githubapp.data.remote.GithubService
import com.example.githubapp.data.remote.SearchPagingSource
import retrofit2.Response

class UserRepositoryImpl constructor(private val githubService: GithubService) : UserRepository {

    override fun getPagingSource(query: String): PagingSource<Int, UserItem> {
        return SearchPagingSource(query, githubService)
    }

    override suspend fun getUserDetails(userName: String): UserDetail {
        return githubService.userDetail(userName)
    }
}