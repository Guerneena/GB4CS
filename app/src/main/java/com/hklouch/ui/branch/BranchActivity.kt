package com.hklouch.ui.branch

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.hklouch.domain.interactor.branch.BranchesUseCase
import com.hklouch.domain.interactor.branch.BranchesUseCase.Params
import com.hklouch.domain.model.Branch
import com.hklouch.githubrepos4cs.R
import com.hklouch.ui.ResourceBaseActivity
import com.hklouch.ui.ResourceListBaseFragment
import com.hklouch.ui.ResourceListViewModel
import com.hklouch.ui.ResourceListViewModelFactory
import com.hklouch.ui.State
import com.hklouch.ui.model.UiBranchItem
import com.hklouch.ui.model.UiPagingWrapper
import com.hklouch.utils.getViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject


class BranchActivity : ResourceBaseActivity(), ResourceListBaseFragment.Delegate<UiBranchItem> {

    @Inject lateinit var viewModelFactory: ResourceListViewModelFactory<Branch,
            UiBranchItem,
            BranchesUseCase,
            BranchesUseCase.Params>
    private lateinit var viewModel: ResourceListViewModel<Branch,
            UiBranchItem,
            BranchesUseCase.Params>


    /* ***************** */
    /*     Lifecycle     */
    /* ***************** */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidInjection.inject(this)

        viewModel = getViewModel { viewModelFactory.supply(Params(ownerName, projectName)) }

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.repo_list_container, BranchFragment())
                    .commit()
        }
    }

    /* ***************** */
    /*  RepoList events  */
    /* ***************** */

    override fun onNextPageRequested(next: Int) {
        viewModel.lastQuery()?.let {
            viewModel.fetchResource(it.copy(page = next))
        }
    }

    override fun onRetryRequested(next: Int) {
        onNextPageRequested(next)
    }

    override fun onRequestDataSubscription(observer: Observer<State<UiPagingWrapper<UiBranchItem>>>) {
        viewModel.getResourceResult().observe(this, observer)
    }

    override fun onLoadSuccess() {
        //nothing
    }
}