package com.hklouch.ui.branch

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hklouch.githubrepos4cs.R
import com.hklouch.ui.ResourceBaseAdapter
import com.hklouch.ui.branch.BranchAdapter.ViewHolder
import com.hklouch.ui.model.UiBranchItem
import javax.inject.Inject

class BranchAdapter @Inject constructor() : ResourceBaseAdapter<UiBranchItem, ViewHolder>() {

    private var models: List<UiBranchItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.branch_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = models.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = models[position]
        holder.branchNameText.text = item.name
        holder.branchUrlText.text = item.url

        /*holder.itemView.setOnClickListener {
            itemListener?.onItemClicked(item)
        }*/
    }

    override fun bindItems(items: List<UiBranchItem>) {
        models += items
    }

    override fun clearData() {
        models = listOf()
    }

    override fun containsElements() = itemCount > 0

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var branchNameText: TextView = view.findViewById(R.id.branch_name_text)
        var branchUrlText: TextView = view.findViewById(R.id.branch_url_text)
    }
}