package com.hklouch.presentation.browse

import com.hklouch.domain.interactor.browse.GetProjectsUseCase
import com.hklouch.domain.interactor.browse.GetProjectsUseCase.Params
import com.hklouch.domain.model.Project
import com.hklouch.presentation.ResourceListViewModel
import com.hklouch.presentation.ResourceListViewModelFactory
import com.hklouch.ui.model.UiProjectPreviewItem
import kotlin.reflect.KFunction1

class BrowseViewModelFactory(private val useCase: GetProjectsUseCase,
                             private val mapper: KFunction1<Project, UiProjectPreviewItem>) :
        ResourceListViewModelFactory<Project, UiProjectPreviewItem, Params>() {

    override fun supply(params: Params?): ResourceListViewModel<Project, UiProjectPreviewItem, Params> {
        return BrowseViewModel(useCase = useCase, mapper = mapper, params = params)
    }
}

class BrowseViewModel(useCase: GetProjectsUseCase, mapper: KFunction1<Project, UiProjectPreviewItem>, params: Params?) :
        ResourceListViewModel<Project, UiProjectPreviewItem, Params>(useCase, mapper, params)