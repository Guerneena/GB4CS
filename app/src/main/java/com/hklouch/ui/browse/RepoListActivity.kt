package com.hklouch.ui.browse

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.hklouch.di.getViewModel
import com.hklouch.githubrepos4cs.R
import com.hklouch.ui.State
import com.hklouch.ui.model.UiPagingModel
import com.hklouch.ui.model.UiProjectItem
import com.hklouch.ui.search.SearchActivity
import dagger.android.AndroidInjection
import javax.inject.Inject


class RepoListActivity : AppCompatActivity(), ReposListFragment.Delegate {

    @Inject lateinit var viewModelFactory: RepoListViewModelFactory
    private lateinit var viewModel: RepoListViewModel

    /* ***************** */
    /*     Life cycle    */
    /* ***************** */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        viewModel = getViewModel { viewModelFactory.supply() }

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(android.R.id.content, ReposListFragment())
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

    override fun onNextPageRequested(next: Int) {
        viewModel.fetchPublicRepos(next)
    }

    override fun onRetryRequested(next: Int) {
        onNextPageRequested(next)
    }

    override fun onObservePublicRepos(observer: Observer<State<UiPagingModel>>) {
        viewModel.getPublicRepos().observe(this, observer)
    }

    override fun onLoadSuccess() {
        //nothing
    }
}

