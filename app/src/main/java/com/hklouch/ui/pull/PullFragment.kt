package com.hklouch.ui.pull

import android.content.Context
import com.hklouch.ui.ResourceBaseAdapter
import com.hklouch.ui.ResourceBaseAdapter.ItemListener
import com.hklouch.ui.ResourceListBaseFragment
import com.hklouch.ui.ResourcePagingAdapter.RefreshCallbacks
import com.hklouch.ui.model.UiPullItem
import com.hklouch.ui.pull.PullAdapter.ViewHolder
import dagger.android.AndroidInjection

class PullFragment : ResourceListBaseFragment<UiPullItem, ViewHolder>() {

    override lateinit var adapter: ResourceBaseAdapter<UiPullItem, ViewHolder>

    override val itemListener: ItemListener<UiPullItem> = object : ItemListener<UiPullItem> {
        override fun onItemClicked(item: UiPullItem) {
            // do nothing
        }
    }

    override val refreshCallbacks: RefreshCallbacks = object : RefreshCallbacks {
        override fun onLoadNext(nextPosition: Int) {
            delegate?.onNextPageRequested(nextPosition)
        }

        override fun onRetry(nextPosition: Int) {
            onLoadNext(nextPosition)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidInjection.inject(this)
        adapter = PullAdapter()
    }
}