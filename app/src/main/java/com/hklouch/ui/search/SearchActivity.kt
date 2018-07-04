package com.hklouch.ui.search

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView.OnQueryTextListener
import com.hklouch.utils.getViewModel
import com.hklouch.githubrepos4cs.R
import com.hklouch.ui.State
import com.hklouch.ui.browse.ReposListFragment
import com.hklouch.ui.model.UiPagingModel
import com.hklouch.utils.hideKeyboard
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.search_activity.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity(), ReposListFragment.Delegate {

    companion object {
        fun createIntent(source: Activity) = Intent(source, SearchActivity::class.java)
    }

    @Inject lateinit var viewModelFactory: SearchViewModelFactory
    private lateinit var viewModel: SearchViewModel

    /* ***************** */
    /*     Life cycle    */
    /* ***************** */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        viewModel = getViewModel { viewModelFactory.supply() }

        setContentView(R.layout.search_activity)

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.repo_list_container, ReposListFragment())
                    .commit()
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupSearchView()
    }

    override fun onSupportNavigateUp() = finish().let { true }

    /* **************** */
    /*      Private     */
    /* **************** */

    private fun setupSearchView() {
        search_toolbar_searchview.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(text: String): Boolean {
                (fragmentManager.findFragmentById(R.id.repo_list_container) as? ReposListFragment)?.clearData()
                viewModel.submitQuery(text)
                return true
            }

            override fun onQueryTextChange(text: String): Boolean {
                return true
            }
        })
    }

    /* ***************** */
    /*  RepoList events  */
    /* ***************** */

    override fun onNextPageRequested(next: Int) {
        viewModel.requestNextPage(next)
    }

    override fun onRetryRequested(next: Int) {
        viewModel.retry()
    }

    override fun onObserveProjects(observer: Observer<State<UiPagingModel>>) {
        viewModel.getResultRepos().observe(this, observer)
    }

    override fun onLoadSuccess() {
        search_toolbar_searchview.apply {
            hideKeyboard()
            clearFocus()
        }
    }

}
