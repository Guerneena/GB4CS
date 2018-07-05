package com.hklouch.domain.model

/**
 * A wrapper that add paging capabilities to a simple response
 */
class PagingWrapper<T>(val nextPage: Int? = null,
                       val lastPage: Int? = null,
                       private val items: List<T>) : Iterable<T> by items