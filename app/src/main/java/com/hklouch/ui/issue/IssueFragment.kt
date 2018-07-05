package com.hklouch.ui.issue

import android.content.Context
import com.hklouch.ui.ResourceBaseAdapter
import com.hklouch.ui.ResourceBaseAdapter.ItemListener
import com.hklouch.ui.ResourceListBaseFragment
import com.hklouch.ui.ResourcePagingAdapter.RefreshCallbacks
import com.hklouch.ui.issue.IssueAdapter.ViewHolder
import com.hklouch.ui.model.UiIssueItem
import dagger.android.AndroidInjection

class IssueFragment : ResourceListBaseFragment<UiIssueItem, ViewHolder>() {

    override lateinit var adapter: ResourceBaseAdapter<UiIssueItem, ViewHolder>

    override val itemListener: ItemListener<UiIssueItem> = object : ItemListener<UiIssueItem> {
        override fun onItemClicked(item: UiIssueItem) {
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
        adapter = IssueAdapter()
    }
}