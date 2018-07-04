package com.hklouch.ui.browse

import android.app.Fragment
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hklouch.domain.model.Project
import com.hklouch.githubrepos4cs.R
import com.hklouch.ui.State
import com.hklouch.ui.State.Error
import com.hklouch.ui.State.Loading
import com.hklouch.ui.State.Success
import com.hklouch.ui.browse.PagingRecyclerAdapter.RefreshCallbacks
import com.hklouch.ui.detail.ProjectDetailActivity
import com.hklouch.ui.model.UiPagingModel
import com.hklouch.utils.hide
import com.hklouch.utils.rootView
import com.hklouch.utils.show
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.repo_list_fragment.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE


class ReposListFragment : Fragment() {

    @Inject lateinit var browseProjectsAdapter: BrowseProjectsAdapter

    private var delegate: Delegate? = null

    private val pagingAdapter by lazy(NONE) {
        PagingRecyclerAdapter(adapter =
                              browseProjectsAdapter.apply {
                                  projectItemListener = this@ReposListFragment.projectListener
                                  setHasStableIds(true)
                              },
                              refreshCallbacks = refreshCallbacks).apply { repo_list.adapter = this }
    }

    /* ***************** */
    /*     Life cycle    */
    /* ***************** */

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidInjection.inject(this)
        delegate = activity as Delegate
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.repo_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupProjectsRecycler()
        delegate?.onObserveProjects(Observer {
            it?.let { handleState(it) }
        })
    }

    override fun onDetach() {
        delegate = null
        super.onDetach()
    }

    /* ***************** */
    /*       Private     */
    /* ***************** */

    private fun handleState(resource: State<UiPagingModel>) {
        when (resource) {
            is Success -> displaySuccess(resource.data)
            is Loading -> {
                if (!browseProjectsAdapter.containsElements()) progress.show()
            }
            is Error -> {
                progress.hide()

                pagingAdapter.onError(getString(R.string.repo_list_error))

                Snackbar.make(rootView,
                              R.string.repo_list_error,
                              Snackbar.LENGTH_SHORT).show()
                Timber.e(resource.throwable)
            }
        }
    }

    private fun setupProjectsRecycler() {
        repo_list.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun displaySuccess(projects: UiPagingModel) {

        progress.hide()
        projects.let {
            pagingAdapter.nextPosition = it.nextPage ?: 0
            browseProjectsAdapter.updateProjects(it.projects)
            browseProjectsAdapter.notifyDataSetChanged()
            repo_list.show()
        }

        delegate?.onLoadSuccess()
    }

    fun clearData() {
        browseProjectsAdapter.clearData()
        pagingAdapter.nextPosition = 0
    }

    private val projectListener = object : ProjectListener {

        override fun onProjectClicked(project: Project) {
            startActivity(ProjectDetailActivity.createIntent(activity, ownerName = project.ownerName, projectName = project.name))
        }
    }

    private val refreshCallbacks = object : RefreshCallbacks {
        override fun onLoadNext(nextPosition: Int) {
            delegate?.onNextPageRequested(nextPosition)
        }

        override fun onRetry(nextPosition: Int) {
            onLoadNext(nextPosition)
        }
    }

    /* ***************** */
    /*      Delegate     */
    /* ***************** */

    interface Delegate {

        fun onNextPageRequested(next: Int)
        fun onRetryRequested(next: Int)
        fun onObserveProjects(observer: Observer<State<UiPagingModel>>)
        fun onLoadSuccess()
    }


}