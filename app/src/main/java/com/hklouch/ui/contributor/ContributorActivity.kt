package com.hklouch.ui.contributor

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.hklouch.domain.interactor.contributor.ContributorsUseCase
import com.hklouch.domain.interactor.contributor.ContributorsUseCase.Params
import com.hklouch.domain.model.User
import com.hklouch.githubrepos4cs.R
import com.hklouch.ui.ResourceBaseActivity
import com.hklouch.ui.ResourceListBaseFragment
import com.hklouch.ui.ResourceListViewModel
import com.hklouch.ui.ResourceListViewModelFactory
import com.hklouch.ui.State
import com.hklouch.ui.model.UiPagingWrapper
import com.hklouch.ui.model.UiUserItem
import com.hklouch.utils.getViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject


class ContributorActivity : ResourceBaseActivity(), ResourceListBaseFragment.Delegate<UiUserItem> {

    @Inject lateinit var viewModelFactory: ResourceListViewModelFactory<User,
            UiUserItem,
            ContributorsUseCase,
            ContributorsUseCase.Params>
    private lateinit var viewModel: ResourceListViewModel<User,
            UiUserItem,
            ContributorsUseCase.Params>


    /* ***************** */
    /*     Lifecycle     */
    /* ***************** */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidInjection.inject(this)

        viewModel = getViewModel { viewModelFactory.supply(Params(ownerName, projectName)) }

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.repo_list_container, ContributorFragment())
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

    override fun onRequestDataSubscription(observer: Observer<State<UiPagingWrapper<UiUserItem>>>) {
        viewModel.getResourceResult().observe(this, observer)
    }

    override fun onLoadSuccess() {
        //nothing
    }
}