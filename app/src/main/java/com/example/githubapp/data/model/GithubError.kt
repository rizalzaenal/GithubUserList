package com.example.githubapp.data.model

import com.google.gson.annotations.SerializedName

data class GithubError(
    @SerializedName("message")
    val message: String?,
    @SerializedName("documentation_url")
    val documentationUrl: String?
)