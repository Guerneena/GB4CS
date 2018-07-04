package com.hklouch.ui.detail

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.hklouch.githubrepos4cs.R
import com.hklouch.ui.State
import com.hklouch.ui.State.Error
import com.hklouch.ui.State.Loading
import com.hklouch.ui.State.Success
import com.hklouch.ui.model.UiProjectItem
import com.hklouch.utils.getViewModel
import com.hklouch.utils.rootView
import dagger.android.AndroidInjection
import timber.log.Timber
import javax.inject.Inject

class ProjectDetailActivity : AppCompatActivity() {

    companion object {

        private const val OWNER_NAME_EXTRA = "owner"
        private const val PROJECT_NAME_EXTRA = "project"

        fun createIntent(source: Activity, ownerName: String, projectName: String): Intent {
            return Intent(source, ProjectDetailActivity::class.java)
                    .apply {
                        putExtra(OWNER_NAME_EXTRA, ownerName)
                        putExtra(PROJECT_NAME_EXTRA, projectName)
                    }
        }
    }

    @Inject lateinit var viewModelFactory: ProjectViewModelFactory
    private lateinit var detailViewModel: ProjectDetailViewModel

    private lateinit var projectDetailViewManager: ProjectDetailViewManager

    /* ***************** */
    /*     Life cycle    */
    /* ***************** */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.project_detail_activity)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailViewModel = getViewModel {
            viewModelFactory.supply(intent.getStringExtra(OWNER_NAME_EXTRA),
                                    intent.getStringExtra(PROJECT_NAME_EXTRA))
        }

        projectDetailViewManager = ProjectDetailViewManager(rootView)
    }

    override fun onResume() {
        super.onResume()
        detailViewModel.getResultProjectDetail().observe(this, Observer {
            it?.let { handleState(it) }
        })
    }

    override fun onSupportNavigateUp() = finish().let { true }

    private fun handleState(resource: State<UiProjectItem>) {
        when (resource) {
            is Success -> displaySuccess(resource.data)
            is Loading -> projectDetailViewManager.isLoading = true
            is Error -> {
                projectDetailViewManager.isErrorState = true
                Snackbar.make(rootView,
                              R.string.repo_list_error,
                              Snackbar.LENGTH_SHORT).show()
                Timber.e(resource.throwable)
            }
        }
    }

    private fun displaySuccess(project: UiProjectItem) {
        projectDetailViewManager.bind(project)
    }
}
