package com.example.githubapp.data.util

import kotlin.math.roundToInt

fun calculateNextPage(currentPage: Int, totalResult: Int, itemsPerPage: Int): Int? {

    val totalPage = (totalResult.toDouble() / itemsPerPage).roundToInt()
    return if (currentPage == totalPage) {
        null
    } else {
        currentPage + 1
    }
}