package com.hklouch.ui

import android.app.Fragment
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hklouch.githubrepos4cs.R
import com.hklouch.ui.ResourceBaseAdapter.ItemListener
import com.hklouch.ui.ResourcePagingAdapter.RefreshCallbacks
import com.hklouch.ui.State.Error
import com.hklouch.ui.State.Loading
import com.hklouch.ui.State.Success
import com.hklouch.ui.model.UiPagingWrapper
import com.hklouch.utils.hide
import com.hklouch.utils.rootView
import com.hklouch.utils.show
import kotlinx.android.synthetic.main.resource_list_fragment.*
import timber.log.Timber
import kotlin.LazyThreadSafetyMode.NONE


abstract class ResourceListBaseFragment<T, U : ViewHolder> : Fragment() {

    abstract var adapter: ResourceBaseAdapter<T, U>

    abstract val itemListener: ItemListener<T>

    abstract val refreshCallbacks: RefreshCallbacks

    protected var delegate: Delegate<T>? = null

    private val pagingAdapter by lazy(NONE) {
        ResourcePagingAdapter(adapter =
                              adapter.apply {
                                  setHasStableIds(true)
                                  itemListener = this@ResourceListBaseFragment.itemListener
                              },
                              refreshCallbacks = refreshCallbacks).apply { resource_list_recycler.adapter = this }
    }
    /* ***************** */
    /*     Life cycle    */
    /* ***************** */

    @Suppress("UNCHECKED_CAST")
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        delegate = activity as? Delegate<T>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.resource_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupProjectsRecycler()
        delegate?.onRequestDataSubscription(Observer {
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

    private fun handleState(resource: State<UiPagingWrapper<T>>) {
        when (resource) {
            is Success -> displaySuccess(resource.data)
            is Loading -> {
                if (!adapter.containsElements()) resource_list_progress.show()
            }
            is Error -> {
                resource_list_progress.hide()

                pagingAdapter.onError(getString(R.string.repo_list_error))

                Snackbar.make(rootView,
                              R.string.repo_list_error,
                              Snackbar.LENGTH_SHORT).show()
                Timber.e(resource.throwable)
            }
        }
    }

    private fun setupProjectsRecycler() {
        resource_list_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun displaySuccess(items: UiPagingWrapper<T>) {

        resource_list_progress.hide()
        items.let {
            pagingAdapter.nextPosition = it.nextPage ?: 0
            adapter.bindItems(it.items)
            adapter.notifyDataSetChanged()
            resource_list_recycler.show()
        }

        delegate?.onLoadSuccess()
    }

    fun clearData() {
        adapter.clearData()
        pagingAdapter.nextPosition = 0
    }


    /* ***************** */
    /*      Delegate     */
    /* ***************** */

    interface Delegate<T> {

        fun onNextPageRequested(next: Int)
        fun onRetryRequested(next: Int)
        fun onRequestDataSubscription(observer: Observer<State<UiPagingWrapper<T>>>)
        fun onLoadSuccess()
    }

}