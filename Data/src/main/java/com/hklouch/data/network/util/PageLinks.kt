package com.hklouch.data.network.util

import okhttp3.Headers
import okhttp3.HttpUrl
import java.net.URL
import java.util.Collections.emptyMap


/**
 * Parse links from executed method
 *
 * @param headers
 */
//TODO Unit test
class PageLinks(headers: Headers) {

    var first: URL? = null
        private set

    var last: URL? = null
        private set

    var next: URL? = null
        private set

    var prev: URL? = null
        private set

    init {
        val linkHeader: String? = headers.get(HEADER_LINK)
        if (linkHeader != null) {
            val links = linkHeader.split(DELIM_LINKS)
            for (link in links) {
                val segments = link.split(DELIM_LINK_PARAM)
                if (segments.size < 2)
                    continue

                var linkPart = segments[0].trim()
                if (!linkPart.startsWith("<") || !linkPart.endsWith(">"))
                    continue
                linkPart = linkPart.substring(1, linkPart.length - 1)

                for (i in 1 until segments.size) {
                    val rel = segments[i].trim().split("=")
                    if (rel.size < 2 || META_REL != rel[0])
                        continue

                    var relValue = rel[1]
                    if (relValue.startsWith("\"") && relValue.endsWith("\""))
                        relValue = relValue.substring(1, relValue.length - 1)

                    val url = linkPart.toUrl()

                    when {
                        META_FIRST == relValue -> first = url
                        META_LAST == relValue -> last = url
                        META_NEXT == relValue -> next = url
                        META_PREV == relValue -> prev = url
                    }
                }
            }
        } else {
            next = headers.get(HEADER_NEXT).toUrl()
            last = headers.get(HEADER_LAST).toUrl()
        }
    }

    /* **************** */
    /*      Methods     */
    /* **************** */

    fun getPagingParamValue(url: URL, param: String): Int? {
        return parametersFromUrl(url)[param]?.firstOrNull { it != null }.toIntOrNull()
    }

    private fun parametersFromUrl(url: URL): MutableMap<String, MutableList<String?>> {
        val m = mutableMapOf<String, MutableList<String?>>()
        return if (url.query.isNullOrBlank()) {
            emptyMap()
        } else url.query.split("&")
                .map(this::extractParameterMap)
                .groupByTo(m, { it.first }, { it.second })
    }


    private fun extractParameterMap(it: String): Pair<String, String?> {
        val idx = it.indexOf("=")
        val key = if (idx > 0) it.substring(0, idx) else it
        val value = if (idx > 0 && it.length > idx + 1) it.substring(idx + 1) else null
        return key to value
    }

    /* ***************** */
    /*     Extensions    */
    /* ***************** */

    private fun String?.toIntOrNull(): Int? = this?.let {
        try {
            it.toInt()
        } catch (e: NumberFormatException) {
            null
        }
    }

    private fun String?.toUrl(): URL? = this?.let {
        HttpUrl.parse(this)?.url()
    }

    companion object {
        private const val DELIM_LINKS = ","
        private const val DELIM_LINK_PARAM = ";"

        private const val META_REL = "rel"
        private const val HEADER_LINK = "Link"
        private const val HEADER_NEXT = "X-Next"
        private const val HEADER_LAST = "X-Last"


        private const val META_FIRST = "first"
        private const val META_LAST = "last"
        private const val META_NEXT = "next"
        private const val META_PREV = "prev"

    }
}

fun PageLinks.nextPage(param: String) = next?.let { getPagingParamValue(it, param) }
fun PageLinks.lastPage(param: String) = last?.let { getPagingParamValue(it, param) }