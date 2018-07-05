package com.hklouch.ui.browse

import android.content.Context
import com.hklouch.ui.ResourceBaseActivity
import com.hklouch.ui.ResourceBaseAdapter
import com.hklouch.ui.ResourceBaseAdapter.ItemListener
import com.hklouch.ui.ResourceListBaseFragment
import com.hklouch.ui.ResourcePagingAdapter.RefreshCallbacks
import com.hklouch.ui.browse.BrowseProjectsAdapter.ViewHolder
import com.hklouch.ui.model.UiProjectPreviewItem
import com.hklouch.ui.pull.PullActivity
import dagger.android.AndroidInjection

class BrowseFragment : ResourceListBaseFragment<UiProjectPreviewItem, ViewHolder>() {

    override lateinit var adapter: ResourceBaseAdapter<UiProjectPreviewItem, ViewHolder>

    override val itemListener: ItemListener<UiProjectPreviewItem> = object : ItemListener<UiProjectPreviewItem> {
        override fun onItemClicked(item: UiProjectPreviewItem) {

            startActivity(ResourceBaseActivity.createIntent(activity,
                                                            PullActivity::class.java,
                                                            ownerName = item.ownerName,
                                                            projectName = item.name))
            /*startActivity(ResourceBaseActivity.createIntent(activity,
                                                            ContributorActivity::class.java,
                                                            ownerName = item.ownerName,
                                                            projectName = item.name))*/
//            startActivity(ResourceBaseActivity.createIntent(activity, BranchActivity::class.java, ownerName = item.ownerName, projectName = item.name))
//            startActivity(ProjectDetailActivity.createIntent(activity, ownerName = item.ownerName, projectName = item.name))
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
        adapter = BrowseProjectsAdapter()
    }
}