package com.hklouch.ui.model

import com.hklouch.domain.model.PagingWrapper
import kotlin.reflect.KFunction1

class UiPagingWrapper<T>(val nextPage: Int? = null,
                         val lastPage: Int? = null,
                         val items: List<T>) : Iterable<T> by items

fun <T, R> PagingWrapper<T>.toUiPagingWrapper(mapper: KFunction1<T, R>) = UiPagingWrapper(nextPage = nextPage,
                                                                                          lastPage = lastPage,
                                                                                          items = this.map(mapper))