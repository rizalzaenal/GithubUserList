package com.example.githubapp.ui.search_user

import java.lang.Exception

sealed class UIState<out T> {
    object Loading : UIState<Nothing>()
    data class Error(val throwable: Throwable) : UIState<Nothing>()
    data class Success<T>(val data: T) : UIState<T>()
}
