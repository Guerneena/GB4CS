package com.hklouch.presentation.branch

import com.hklouch.domain.interactor.branch.BranchesUseCase
import com.hklouch.domain.interactor.branch.BranchesUseCase.Params
import com.hklouch.domain.model.Branch
import com.hklouch.presentation.ResourceListViewModel
import com.hklouch.presentation.ResourceListViewModelFactory
import com.hklouch.ui.model.UiBranchItem
import kotlin.reflect.KFunction1

class BranchViewModelFactory(private val useCase: BranchesUseCase,
                             private val mapper: KFunction1<Branch, UiBranchItem>) :
        ResourceListViewModelFactory<Branch, UiBranchItem, Params>() {

    override fun supply(params: Params?): ResourceListViewModel<Branch, UiBranchItem, Params> {
        return BranchViewModel(useCase = useCase, mapper = mapper, params = params)
    }
}

class BranchViewModel(useCase: BranchesUseCase, mapper: KFunction1<Branch, UiBranchItem>, params: Params?) :
        ResourceListViewModel<Branch, UiBranchItem, Params>(useCase, mapper, params)