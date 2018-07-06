package com.hklouch.presentation.search

import com.hklouch.domain.interactor.search.SearchProjectsUseCase
import com.hklouch.domain.interactor.search.SearchProjectsUseCase.Params
import com.hklouch.domain.model.Project
import com.hklouch.presentation.ResourceListViewModel
import com.hklouch.presentation.ResourceListViewModelFactory
import com.hklouch.ui.model.UiProjectPreviewItem
import kotlin.reflect.KFunction1

class SearchViewModelFactory(private val useCase: SearchProjectsUseCase,
                             private val mapper: KFunction1<Project, UiProjectPreviewItem>) :
        ResourceListViewModelFactory<Project, UiProjectPreviewItem, Params>() {

    override fun supply(params: Params?): ResourceListViewModel<Project, UiProjectPreviewItem, Params> {
        return SearchViewModel(useCase = useCase, mapper = mapper, params = params)
    }
}

class SearchViewModel(useCase: SearchProjectsUseCase, mapper: KFunction1<Project, UiProjectPreviewItem>, params: Params?) :
        ResourceListViewModel<Project, UiProjectPreviewItem, Params>(useCase, mapper, params, false)