package com.hklouch.ui.branch

import android.content.Context
import com.hklouch.ui.ResourceBaseAdapter
import com.hklouch.ui.ResourceBaseAdapter.ItemListener
import com.hklouch.ui.ResourceListBaseFragment
import com.hklouch.ui.ResourcePagingAdapter.RefreshCallbacks
import com.hklouch.ui.branch.BranchAdapter.ViewHolder
import com.hklouch.ui.model.UiBranchItem
import dagger.android.AndroidInjection

class BranchFragment : ResourceListBaseFragment<UiBranchItem, ViewHolder>() {

    override lateinit var adapter: ResourceBaseAdapter<UiBranchItem, ViewHolder>

    override val itemListener: ItemListener<UiBranchItem> = object : ItemListener<UiBranchItem> {
        override fun onItemClicked(item: UiBranchItem) {
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
        adapter = BranchAdapter()
    }
}