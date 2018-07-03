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
import com.hklouch.githubrepos4cs.R
import com.hklouch.ui.State
import com.hklouch.ui.State.Error
import com.hklouch.ui.State.Loading
import com.hklouch.ui.State.Success
import com.hklouch.ui.browse.PagingRecyclerAdapter.RefreshCallbacks
import com.hklouch.ui.model.UiPagingModel
import com.hklouch.utils.rootView
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.repo_list_fragment.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE


class ReposListFragment : Fragment() {

    @Inject lateinit var browseAdapter: BrowseAdapter

    private var delegate: Delegate? = null

    private val pagingAdapter by lazy(NONE) {
        PagingRecyclerAdapter(adapter =
                              browseAdapter.apply {
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
        setupPublicReposRecycler()
    }


    override fun onResume() {
        super.onResume()
        delegate?.onObservePublicRepos(Observer {
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
                if (!browseAdapter.containsElements()) progress.visibility = View.VISIBLE
            }
            is Error -> {
                progress.visibility = View.GONE
                repo_list.visibility = View.GONE

                pagingAdapter.onError(getString(R.string.repo_list_error))

                Snackbar.make(rootView,
                              R.string.repo_list_error,
                              Snackbar.LENGTH_SHORT).show()
                Timber.e(resource.throwable)
            }
        }
    }

    private fun setupPublicReposRecycler() {
        repo_list.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun displaySuccess(projects: UiPagingModel) {

        progress.visibility = View.GONE
        projects.let {
            //            pagingAdapter.maxItems = it.lastPage ?: 0
            pagingAdapter.nextPosition = it.nextPage ?: 0
            browseAdapter.updateProjects(it.projects)
            browseAdapter.notifyDataSetChanged()
            repo_list.visibility = View.VISIBLE
        }

        delegate?.onLoadSuccess()
    }

    fun clearData() {
        browseAdapter.clearData()
        pagingAdapter.nextPosition = 0
    }

    private val projectListener = object : ProjectListener {

        override fun onProjectClicked(projectId: Int) {
            //TODO goto details
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
        fun onObservePublicRepos(observer: Observer<State<UiPagingModel>>)
        fun onLoadSuccess()
    }


}