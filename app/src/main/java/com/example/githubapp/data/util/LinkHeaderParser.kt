package com.example.githubapp.data.util

class LinkHeaderParser(linkHeader: String?) {
    var nextUrl: String? = null

    init {
        linkHeader?.split(",")?.forEach { segment ->
            var url: String? = null
            segment.split(";").forEach { rel ->
                if (rel.startsWith("<") && rel.endsWith(">")) {
                    url = rel.substring(1, rel.length - 1) // take string between < and >
                }
                if (rel.contains("next")) {
                    //if this relation is next relation, asign url inside < and > to nextUrl variable
                    nextUrl = url
                }
            }
        }
    }

}