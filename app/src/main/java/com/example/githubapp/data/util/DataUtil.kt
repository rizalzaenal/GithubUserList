package com.example.githubapp.data.util

import com.example.githubapp.data.model.GithubError
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException
import kotlin.math.roundToInt

fun calculateNextPage(currentPage: Int, totalResult: Int, itemsPerPage: Int): Int? {
    val totalPage = (totalResult.toDouble() / itemsPerPage).roundToInt()
    return if (currentPage == totalPage) {
        null
    } else {
        currentPage + 1
    }
}

fun convertException(throwable: Throwable): GithubError {
    when (throwable) {
        is HttpException -> {
            return try {
                val error = throwable.response()?.errorBody()?.string()
                val response = Gson().fromJson(error, GithubError::class.java)
                response
            } catch (exception: Exception) {
                GithubError(null, null)
            }
        }
        is IOException -> {
            return GithubError(message = "No Internet, please check your connections...", null)
        }
        else -> {
            return GithubError(message = "Unknown Error", null)
        }
    }

}