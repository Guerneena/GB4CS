package com.hklouch.ui.model

import com.hklouch.domain.model.ProjectList

data class UiPagingModel(val nextPage: Int?,
                         val lastPage: Int?,
                         val projects: List<UiProjectPreviewItem>)

fun ProjectList.toUiPagingModel() = UiPagingModel(nextPage = nextPage,
                                                  lastPage = lastPage,
                                                  projects = projects.map { it.toUiProjectPreviewItem() })