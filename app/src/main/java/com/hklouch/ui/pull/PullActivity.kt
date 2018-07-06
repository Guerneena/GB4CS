package com.hklouch.ui.pull

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.hklouch.domain.interactor.pull.PullsUseCase.Params
import com.hklouch.githubrepos4cs.R
import com.hklouch.presentation.pull.PullViewModel
import com.hklouch.presentation.pull.PullViewModelFactory
import com.hklouch.ui.ResourceBaseActivity
import com.hklouch.ui.ResourceListBaseFragment
import com.hklouch.ui.State
import com.hklouch.ui.model.UiPagingWrapper
import com.hklouch.ui.model.UiPullItem
import com.hklouch.utils.getViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject
import com.hklouch.domain.interactor.pull.PullsUseCase.Params.State as PullState


class PullActivity : ResourceBaseActivity(), ResourceListBaseFragment.Delegate<UiPullItem> {

    /* **************** */
    /*        DI        */
    /* **************** */
    @Inject lateinit var viewModelFactory: PullViewModelFactory

    private lateinit var viewModel: PullViewModel

    /* ***************** */
    /*     Lifecycle     */
    /* ***************** */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidInjection.inject(this)

        viewModel = getViewModel { viewModelFactory.supply(Params(ownerName, projectName, PullState.OPEN)) } as PullViewModel

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.repo_list_container, PullFragment())
                    .commit()
        }
    }

    /* **************** */
    /*  Content events  */
    /* **************** */

    override fun onNextPageRequested(next: Int) {
        viewModel.lastQuery()?.let {
            viewModel.fetchResource(it.copy(page = next))
        }
    }

    override fun onRetryRequested(next: Int) {
        onNextPageRequested(next)
    }

    override fun onRequestDataSubscription(observer: Observer<State<UiPagingWrapper<UiPullItem>>>) {
        viewModel.getResourceResult().observe(this, observer)
    }

    override fun onLoadSuccess() {
        //nothing
    }
}

