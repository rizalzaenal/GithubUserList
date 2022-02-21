package com.example.githubapp.data.repository

import androidx.paging.PagingSource
import com.example.githubapp.data.model.SearchUser
import com.example.githubapp.data.model.UserDetail
import com.example.githubapp.data.model.UserItem
import retrofit2.Response

interface UserRepository {

    fun getPagingSource(query: String): PagingSource<Int, UserItem>
    suspend fun getUserDetails(userName: String): UserDetail

}