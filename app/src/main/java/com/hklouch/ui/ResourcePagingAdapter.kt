package com.hklouch.ui

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.AdapterDataObserver
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.hklouch.githubrepos4cs.R
import com.hklouch.githubrepos4cs.R.layout
import com.hklouch.ui.ResourcePagingAdapter.State.ERROR
import com.hklouch.ui.ResourcePagingAdapter.State.FINISHED
import com.hklouch.ui.ResourcePagingAdapter.State.IDLE
import com.hklouch.ui.ResourcePagingAdapter.State.LOADING

/**
 * Recycler adapter that adds paging capabilities.
 */
class ResourcePagingAdapter<T : RecyclerView.ViewHolder>(
        private val adapter: RecyclerView.Adapter<T>,
        private val refreshCallbacks: RefreshCallbacks,
        @LayoutRes private val loadingLayout: Int = R.layout.view_paging_adapter_default_loading
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_LOADER_HOLDER = R.id.type_paging_loader
        const val TYPE_ERROR_HOLDER = R.id.type_paging_error
    }

    private val observerAdapter = RelayAdapterObserver(adapter = this, defaultOnChangeBehavior = { onDelegateAdapterContentChanged() })

    var nextPosition = 0
        set(value) {
            field = value
            onDelegateAdapterContentChanged()
        }

    private var currentState: State = LOADING
        set(value) {
            val oldLayoutType = field.viewType
            field = value
            if (value.viewType != oldLayoutType) {
                notifyDataSetChanged()
            }
        }
    private var currentErrorMessage: CharSequence? = null

    private fun onDelegateAdapterContentChanged() {
        currentState = when (nextPosition) {
            0 -> FINISHED
            else -> IDLE
        }
    }


    override fun getItemCount(): Int {
        val itemCount = adapter.itemCount
        return if (nextPosition > 0 || currentState == ERROR) {
            itemCount + 1 // Add a loader or error view at bottom
        } else {
            itemCount
        }
    }

    override fun getItemViewType(position: Int): Int =
            if (position >= adapter.itemCount) {
                currentState.viewType
            } else {
                adapter.getItemViewType(position)
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                TYPE_LOADER_HOLDER -> createLoadingHolder(parent)
                TYPE_ERROR_HOLDER -> createErrorHolder(parent)
                else -> adapter.onCreateViewHolder(parent, viewType)
            }

    private fun createLoadingHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LoadingHolder(inflater.inflate(loadingLayout, parent, false))
    }

    private fun createErrorHolder(parent: ViewGroup): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ErrorHolder(inflater.inflate(layout.view_paging_adapter_error_message, parent, false)) {
            currentState = LOADING
            refreshCallbacks.onRetry(it.adapterPosition)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
            when (holder) {
                is LoadingHolder -> {
                }// Do nothing
                is ErrorHolder -> {
                    holder.setErrorMessage(currentErrorMessage)
                }
                else -> adapter.onBindViewHolder(holder as T, position)
            }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder?) {
        super.onViewAttachedToWindow(holder)

        if (currentState != LOADING && holder is LoadingHolder) {
            currentState = LOADING
            refreshCallbacks.onLoadNext(nextPosition)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        adapter.registerAdapterDataObserver(observerAdapter)
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
        adapter.unregisterAdapterDataObserver(observerAdapter)
    }

    /**
     * Callbacks that will be invoked whenever end of layout is reached
     */
    interface RefreshCallbacks {
        fun onLoadNext(nextPosition: Int)
        fun onRetry(nextPosition: Int)
    }

    fun onError(message: CharSequence) {
        currentState = ERROR
        currentErrorMessage = message
    }

    enum class State(@LayoutRes val viewType: Int) {
        IDLE(TYPE_LOADER_HOLDER),
        LOADING(TYPE_LOADER_HOLDER),
        ERROR(TYPE_ERROR_HOLDER),
        FINISHED(0)
    }
}

/**
 * Simple adapter that observes events on delegate adapter, in order to dispatch them to own observers.
 */
private class RelayAdapterObserver(private val adapter: Adapter<*>, private val defaultOnChangeBehavior: () -> Unit = {}) : AdapterDataObserver() {

    override fun onChanged() {
        defaultOnChangeBehavior()
        adapter.notifyDataSetChanged()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        defaultOnChangeBehavior()
        adapter.notifyItemRangeRemoved(positionStart, itemCount)
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        defaultOnChangeBehavior()
        adapter.notifyItemRangeInserted(positionStart, itemCount)
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        defaultOnChangeBehavior()
        adapter.notifyItemRangeChanged(positionStart, itemCount)
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        defaultOnChangeBehavior()
        adapter.notifyItemRangeChanged(positionStart, itemCount, payload)
    }

}

/**
 * Loading holder that does nothing but show a loader (or a text or anything)
 */
class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class ErrorHolder(itemView: View, onRetry: (ErrorHolder) -> Unit) : ViewHolder(itemView) {

    private val errorTextView: TextView = itemView.findViewById(R.id.error_message)
    private val retryButton: Button = itemView.findViewById(R.id.retry_button)

    init {
        retryButton.setOnClickListener {
            onRetry(this)
        }
    }

    fun setErrorMessage(message: CharSequence?) {
        errorTextView.text = message
    }
}
