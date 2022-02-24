package com.example.githubapp.data.util

import org.junit.Test
import org.junit.Assert.*
class DataUtilTest {

    //Given
    private val dateFromResponse = "2007-10-20T05:24:19Z"
    private val formattedDate = "20 Oct 2007"

    @Test
    fun assert_date_formatted() {
        assertEquals(formattedDate, formatDate(dateFromResponse))
    }

    @Test
    fun assert_date_blank() {
        assertEquals("", formatDate(""))
    }

}