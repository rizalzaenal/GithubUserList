package com.example.githubapp.data.remote

import com.example.githubapp.data.model.SearchUser
import com.example.githubapp.data.model.UserDetail
import com.example.githubapp.data.model.UserItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface GithubService {

    @GET("search/users")
    suspend fun searchUser(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") sizePerPage: Int = 10
    ): Response<SearchUser>

    @GET("users")
    suspend fun users(@Query("per_page") sizePerPage: Int = 10): Response<List<UserItem>>

    @GET
    suspend fun users(@Url url: String, @Query("per_page") sizePerPage: Int = 10): Response<List<UserItem>>

    @GET("users/{username}")
    suspend fun userDetail(@Path("username") userName: String): UserDetail

}