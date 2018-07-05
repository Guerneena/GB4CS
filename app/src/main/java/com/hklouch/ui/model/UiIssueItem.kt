package com.hklouch.ui.model

import com.hklouch.domain.model.Issue

data class UiIssueItem(val url: String,
                       val ownerId: Int,
                       val ownerName: String,
                       val ownerAvatarUrl: String,
                       val title: String,
                       val number: Int,
                       val state: String
)

fun Issue.toUiIssueItem() = UiIssueItem(
        url = url,
        ownerId = ownerId,
        ownerName = ownerName,
        ownerAvatarUrl = ownerAvatarUrl,
        title = title,
        number = number,
        state = state
)


