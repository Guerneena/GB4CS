package com.hklouch.ui.detail

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hklouch.githubrepos4cs.R
import com.hklouch.ui.model.UiProjectItem
import com.hklouch.utils.drawableTop
import com.hklouch.utils.getColorCompat
import com.hklouch.utils.hide
import com.hklouch.utils.show
import com.hklouch.utils.tint

class ProjectDetailViewManager(private val context: Context,
                               view: View) {

    interface ViewDelegate {
        fun onBranchesClick()
        fun onPullsClick()
        fun onIssuesClick()
        fun onContributorsClick()
    }

    private var delegate: ViewDelegate? = context as? ViewDelegate

    private val ownerImageView: ImageView = view.findViewById(R.id.project_detail_owner_image)
    private val ownerNameView: TextView = view.findViewById(R.id.project_detail_owner_name_text)
    private val projectNameView: TextView = view.findViewById(R.id.project_detail_name_text)
    private val projectDescriptionView: TextView = view.findViewById(R.id.project_detail_description)
    private val starCountView: TextView = view.findViewById(R.id.project_detail_stars)
    private val forksCountView: TextView = view.findViewById(R.id.project_detail_forks)
    private val watchersCountView: TextView = view.findViewById(R.id.project_detail_watchers)
    private val isAForkView: TextView = view.findViewById(R.id.project_detail_is_fork)

    private val issuesView: TextView = view.findViewById(R.id.project_detail_issues)
    private val branchesView: TextView = view.findViewById(R.id.project_detail_branches)
    private val pullsView: TextView = view.findViewById(R.id.project_detail_pulls)
    private val contributorsView: TextView = view.findViewById(R.id.project_detail_contributors)

    private val progressBar: ProgressBar = view.findViewById(R.id.project_detail_progress)
    private val contentView: ViewGroup = view.findViewById(R.id.content_project_detail)

    var isLoading: Boolean = false
        set(value) {
            field = value
            if (value) {
                progressBar.show()
                contentView.hide()
            } else {
                progressBar.hide()
                contentView.show()
            }
        }


    var isErrorState: Boolean = false
        set(value) {
            field = value
            if (value) {
                progressBar.hide()
                contentView.hide()
            }
        }


    fun bind(project: UiProjectItem) {
        isLoading = false
        isErrorState = false

        ownerNameView.text = project.ownerName
        projectNameView.text = project.fullName
        projectDescriptionView.text = project.description

        Glide.with(context)
                .load(project.ownerAvatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(ownerImageView)

        starCountView.text = "${project.starsCount}"
        forksCountView.text = "${project.forksCount}"
        watchersCountView.text = "${project.watchersCount}"

        isAForkView.apply {
            if (project.isFork) {
                setText(R.string.project_detail_is_fork)
                drawableTop?.tint(context.getColorCompat(R.color.orange))
            } else {
                setText(R.string.project_detail_not_fork)
                drawableTop?.tint(context.getColorCompat(R.color.grey_border))
            }
        }

        issuesView.apply {
            text = context.getString(R.string.project_detail_issues, project.issuesCount)
            setOnClickListener {
                delegate?.onIssuesClick()
            }
        }
        branchesView.setOnClickListener {
            delegate?.onBranchesClick()
        }
        contributorsView.setOnClickListener {
            delegate?.onContributorsClick()
        }
        pullsView.setOnClickListener {
            delegate?.onPullsClick()
        }
    }
}