package com.example.githubapp.data.remote

import com.example.githubapp.data.model.SearchUser
import com.example.githubapp.data.model.UserDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {

    @GET("search/users")
    suspend fun searchUser(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") sizePerPage: Int = 10
    ): Response<SearchUser>

    @GET("users/{username}")
    suspend fun userDetail(@Path("username") userName: String): Response<UserDetail>

}