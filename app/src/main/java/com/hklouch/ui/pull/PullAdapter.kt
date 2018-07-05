package com.hklouch.ui.pull

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hklouch.githubrepos4cs.R
import com.hklouch.ui.ResourceBaseAdapter
import com.hklouch.ui.model.UiPullItem
import com.hklouch.ui.pull.PullAdapter.ViewHolder
import javax.inject.Inject

class PullAdapter @Inject constructor() : ResourceBaseAdapter<UiPullItem, ViewHolder>() {

    private var models: List<UiPullItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.pull_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = models.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = models[position]
        holder.ownerNameText.text = item.ownerName
        holder.pullTitleText.text = item.title

        Glide.with(holder.itemView.context)
                .load(item.ownerAvatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.ownerImage)

        /*holder.itemView.setOnClickListener {
            itemListener?.onItemClicked(item)
        }*/
    }

    override fun bindItems(items: List<UiPullItem>) {
        models += items
    }

    override fun clearData() {
        models = listOf()
    }

    override fun containsElements() = itemCount > 0

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ownerImage: ImageView = view.findViewById(R.id.pull_owner_image)
        var ownerNameText: TextView = view.findViewById(R.id.pull_owner_name_text)
        var pullTitleText: TextView = view.findViewById(R.id.pull_title_text)
    }
}