package com.example.githubapp.ui.search_user

data class SnackBarUIState(
    val name: String = "",
    val email: String = "",
    val createdAt: String = "",
    val shouldShown: Boolean = false,
    val errorMessage: String = ""
)