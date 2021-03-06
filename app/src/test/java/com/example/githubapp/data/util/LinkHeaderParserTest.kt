package com.example.githubapp.data.util

import org.junit.Assert.*
import org.junit.Test

class LinkHeaderParserTest {

    //Given
    private val nullParser = LinkHeaderParser(null)

    @Test
    fun assert_next_link_is_null() {
        assertEquals(null, nullParser.nextUrl)
    }

    //Given
    private val headerLinkNoNext = "<https://api.github.com/users?since=46>; rel=\"last\", <https://api.github.com/users{?since}>; rel=\"first\""
    private val noNextParser = LinkHeaderParser(headerLinkNoNext)

    @Test
    fun assert_next_link_is_null_if_no_next_link() {
        assertEquals(null, noNextParser.nextUrl)
    }

    //Given
    private val headerLink = "<https://api.github.com/users?since=46>; rel=\"next\", <https://api.github.com/users{?since}>; rel=\"first\""
    private val parser = LinkHeaderParser(headerLink)

    @Test
    fun assert_next_link_is_correct() {
        assertEquals("https://api.github.com/users?since=46", parser.nextUrl)
    }

}