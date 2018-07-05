package com.hklouch.ui.model

import com.hklouch.domain.model.PagingWrapper
import com.hklouch.domain.model.Project

data class UiPagingModel(val nextPage: Int?,
                         val lastPage: Int?,
                         val projects: List<UiProjectPreviewItem>)

fun PagingWrapper<Project>.toUiPagingModel() = UiPagingModel(nextPage = nextPage,
                                                             lastPage = lastPage,
                                                             projects = this.map { it.toUiProjectPreviewItem() })