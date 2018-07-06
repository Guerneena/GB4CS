package com.hklouch.data.network

import com.hklouch.data.network.util.PageLinks
import com.hklouch.data.network.util.lastPage
import com.hklouch.data.network.util.nextPage
import com.hklouch.domain.model.PagingWrapper
import retrofit2.adapter.rxjava2.Result

/**
 * An extension that builds a [PagingWrapper] from a retrofit [Result]
 *
 * @param pagingParameter used to extract paging information from response headers
 * @param mapper lambda to map from data model to domain model
 *
 * @return [PagingWrapper] with paging information
 */
fun <T, R> Result<T>.toPagingWrapper(pagingParameter: String,
                                     mapper: () -> List<R>?): PagingWrapper<R> {
    val resultList = mapper()
    val response = response()
    if (isError || resultList == null || response == null || response.errorBody() != null) {
        error()?.let { throw it } ?: throw IllegalArgumentException("Unknown problem occurred")
    }
    val pageLinks = PageLinks(response.headers())
    val nextIndex = pageLinks.nextPage(pagingParameter)
    val lastIndex = pageLinks.lastPage(pagingParameter)
    return PagingWrapper(nextIndex, lastIndex, resultList)

}