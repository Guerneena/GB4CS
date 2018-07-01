package com.hklouch.ui.browse

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.hklouch.di.getViewModel
import com.hklouch.githubrepos4cs.R
import com.hklouch.ui.State
import com.hklouch.ui.State.Error
import com.hklouch.ui.State.Loading
import com.hklouch.ui.State.Success
import com.hklouch.ui.model.UiProjectItem
import com.hklouch.ui.search.SearchActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.repo_list_activity.*
import kotlinx.android.synthetic.main.repo_list_layout.*
import timber.log.Timber
import javax.inject.Inject

class RepoListActivity : AppCompatActivity() {

    @Inject lateinit var browseAdapter: BrowseAdapter
    @Inject lateinit var viewModelFactory: RepoListViewModelFactory
    private lateinit var viewModel: RepoListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.repo_list_activity)
        viewModel = getViewModel { viewModelFactory.supply() }
        setupPublicReposRecycler()

    }

    override fun onResume() {
        super.onResume()
        viewModel.getPublicRepos().observe(this, Observer<State<List<UiProjectItem>>> {
            it?.let { handleState(it) }
        })
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
        progress.visibility = View.VISIBLE
    }

    private fun displaySuccess(projects: List<UiProjectItem>) {
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

