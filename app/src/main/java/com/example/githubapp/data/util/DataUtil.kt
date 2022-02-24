package com.example.githubapp.data.util

import com.example.githubapp.data.model.GithubError
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

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
            return GithubError(message = throwable.message, null)
        }
    }
}

fun formatDate(date: String): String {
    val dataFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val appFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return try {
        appFormat.format(dataFormat.parse(date) ?: Date())
    } catch (e: ParseException) {
        ""
    }
}

fun String?.replaceNull() : String {
    return this ?: ""
}