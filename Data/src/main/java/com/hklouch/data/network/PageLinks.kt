package com.hklouch.data.network

import okhttp3.Headers

/**
 * Parse links from executed method
 *
 * @param headers
 */
class PageLinks(headers: Headers) {

    var first: String? = null
        private set

    var last: String? = null
        private set

    var next: String? = null
        private set

    var prev: String? = null
        private set

    var lastIndex: Int? = null
        private set

    var nextIndex: Int? = null
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

                    val index: Int? = linkPart.split("=")
                            .let { if (it.size > 1) it.last() else null }
                            .toIntOrNull()

                    when {
                        META_FIRST == relValue -> first = linkPart
                        META_LAST == relValue -> {
                            last = linkPart
                            lastIndex = index
                        }
                        META_NEXT == relValue -> {
                            next = linkPart
                            nextIndex = index
                        }
                        META_PREV == relValue -> prev = linkPart
                    }
                }
            }
        } else {
            next = headers.get(HEADER_NEXT)
            last = headers.get(HEADER_LAST)
        }
    }

    private fun String?.toIntOrNull(): Int? = this?.let {
        try {
            it.toInt()
        } catch (e: NumberFormatException) {
            null
        }
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