package com.hklouch.ui.detail

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hklouch.githubrepos4cs.R
import com.hklouch.ui.model.UiProjectItem
import com.hklouch.utils.drawableTop
import com.hklouch.utils.getColorCompat
import com.hklouch.utils.tint

class ProjectDetailViewManager(view: View, project: UiProjectItem) {
    private val ownerImageView: ImageView = view.findViewById(R.id.owner_image)
    private val ownerNameView: TextView = view.findViewById(R.id.owner_name_text)
    private val projectNameView: TextView = view.findViewById(R.id.project_name_text)
    private val projectDescriptionView: TextView = view.findViewById(R.id.project_detail_description)
    private val starCountView: TextView = view.findViewById(R.id.project_detail_stars)
    private val forksCountView: TextView = view.findViewById(R.id.project_detail_forks)
    private val watchersCountView: TextView = view.findViewById(R.id.project_detail_watchers)
    private val issuesCountView: TextView = view.findViewById(R.id.project_detail_issues)
    private val isAForkView: TextView = view.findViewById(R.id.project_detail_is_fork)

    init {
        ownerNameView.text = project.ownerName
        projectNameView.text = project.fullName
        projectDescriptionView.text = project.description

        Glide.with(view.context)
                .load(project.ownerAvatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(ownerImageView)

        starCountView.text = "${project.starsCount}"
        forksCountView.text = "${project.forksCount}"
        watchersCountView.text = "${project.watchersCount}"
        issuesCountView.text = "${project.issuesCount}"

        isAForkView.apply {
            if (project.isFork) {
                setText(R.string.project_detail_is_fork)
                drawableTop?.tint(view.context.getColorCompat(R.color.orange))
            } else {
                setText(R.string.project_detail_not_fork)
                drawableTop?.tint(view.context.getColorCompat(R.color.grey_border))
            }
        }
    }
}