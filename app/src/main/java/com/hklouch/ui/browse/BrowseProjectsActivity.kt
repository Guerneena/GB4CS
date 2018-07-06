package com.hklouch.ui.browse

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.hklouch.domain.interactor.browse.GetProjectsUseCase.Params
import com.hklouch.githubrepos4cs.R
import com.hklouch.presentation.browse.BrowseViewModel
import com.hklouch.presentation.browse.BrowseViewModelFactory
import com.hklouch.ui.ResourceListBaseFragment
import com.hklouch.ui.State
import com.hklouch.ui.model.UiPagingWrapper
import com.hklouch.ui.model.UiProjectPreviewItem
import com.hklouch.ui.search.SearchActivity
import com.hklouch.utils.getViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.toolbar_layout.*
import javax.inject.Inject


class BrowseProjectsActivity : AppCompatActivity(), ResourceListBaseFragment.Delegate<UiProjectPreviewItem> {

    /* **************** */
    /*        DI        */
    /* **************** */
    @Inject lateinit var viewModelFactory: BrowseViewModelFactory

    private lateinit var viewModel: BrowseViewModel

    /* ***************** */
    /*     Life cycle    */
    /* ***************** */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        viewModel = getViewModel { viewModelFactory.supply() } as BrowseViewModel

        setContentView(R.layout.browse_projects_activity)

        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.repo_list_container, BrowseFragment())
                    .commit()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.repos_list_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.repo_list_menu_search -> {
                startActivity(SearchActivity.createIntent(this))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /* **************** */
    /*  Content events  */
    /* **************** */

    override fun onNextPageRequested(next: Int) {
        viewModel.fetchResource(Params(next))
    }

    override fun onRetryRequested(next: Int) {
        onNextPageRequested(next)
    }

    override fun onRequestDataSubscription(observer: Observer<State<UiPagingWrapper<UiProjectPreviewItem>>>) {
        viewModel.getResourceResult().observe(this, observer)
    }

    override fun onLoadSuccess() {
        //nothing
    }
}

