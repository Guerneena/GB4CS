package com.hklouch.ui.search

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView.OnQueryTextListener
import android.view.View
import com.hklouch.di.getViewModel
import com.hklouch.githubrepos4cs.R
import com.hklouch.ui.State
import com.hklouch.ui.State.Error
import com.hklouch.ui.State.Loading
import com.hklouch.ui.State.Success
import com.hklouch.ui.browse.BrowseAdapter
import com.hklouch.ui.browse.ProjectListener
import com.hklouch.ui.model.UiProjectItem
import com.hklouch.utils.hideKeyboard
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.repo_list_layout.*
import kotlinx.android.synthetic.main.search_activity.*
import timber.log.Timber
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    companion object {
        fun createIntent(source: Activity) = Intent(source, SearchActivity::class.java)
    }

    @Inject lateinit var browseAdapter: BrowseAdapter
    @Inject lateinit var viewModelFactory: SearchViewModelFactory
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        viewModel = getViewModel { viewModelFactory.supply() }

        setContentView(R.layout.search_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupSearchView()
        setupPublicReposRecycler()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getResultRepos().observe(this, Observer<State<List<UiProjectItem>>> {
            it?.let { handleState(it) }
        })
    }

    override fun onSupportNavigateUp() = finish().let { true }

    private fun setupSearchView() {
        search_toolbar_searchview.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(text: String): Boolean {
                viewModel.searchRepos(text)
                return true
            }

            override fun onQueryTextChange(text: String): Boolean {
                return true
            }
        })
    }

    private fun handleState(resource: State<List<UiProjectItem>>) {
        when (resource) {
            is Success -> displaySuccess(resource.data)
            is Loading -> {
                progress.visibility = View.VISIBLE
                repo_list.visibility = View.GONE
            }
            is Error -> {
                progress.visibility = View.GONE
                repo_list.visibility = View.GONE

                Snackbar.make(findViewById(android.R.id.content),
                              R.string.repo_list_error,
                              Snackbar.LENGTH_INDEFINITE).show()
                Timber.e(resource.throwable)
            }
        }
    }

    private fun setupPublicReposRecycler() {
        browseAdapter.projectListener = projectListener
        repo_list.layoutManager = LinearLayoutManager(this)
        repo_list.adapter = browseAdapter
    }

    private fun displaySuccess(projects: List<UiProjectItem>) {
        search_toolbar_searchview.apply {
            hideKeyboard()
            clearFocus()
        }
        progress.visibility = View.GONE
        projects.let {
            browseAdapter.projects = it
            browseAdapter.notifyDataSetChanged()
            repo_list.visibility = View.VISIBLE
        }
    }

    private val projectListener = object : ProjectListener {

        override fun onProjectClicked(projectId: String) {
            //TODO goto details
        }
    }


}
