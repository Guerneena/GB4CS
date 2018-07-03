package com.hklouch.ui.browse

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hklouch.githubrepos4cs.R
import com.hklouch.ui.browse.BrowseAdapter.ViewHolder
import com.hklouch.ui.model.UiProjectItem
import javax.inject.Inject

class BrowseAdapter @Inject constructor() : RecyclerView.Adapter<ViewHolder>() {

    var projects: List<UiProjectItem> = listOf()
    var projectItemListener: ProjectListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.repo_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = projects.count()

    override fun getItemId(position: Int) = projects[position].id.toLong()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val project = projects[position]
        holder.ownerNameText.text = project.ownerName
        holder.projectNameText.text = project.fullName

        Glide.with(holder.itemView.context)
                .load(project.ownerAvatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.ownerImage)


        holder.itemView.setOnClickListener {
            projectItemListener?.onProjectClicked(project.id)
        }
    }

    fun updateProjects(newProjects: List<UiProjectItem>) {
        projects += newProjects
    }

    fun clearData() {
        projects = listOf()
    }


    fun containsElements() = itemCount > 0

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ownerImage: ImageView = view.findViewById(R.id.owner_image)
        var ownerNameText: TextView = view.findViewById(R.id.owner_name_text)
        var projectNameText: TextView = view.findViewById(R.id.project_name_text)
    }

}