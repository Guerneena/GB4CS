package com.hklouch.data.network.issue

import com.hklouch.domain.model.Issue

fun IssueJson.toIssue() = Issue(url = url,
                                ownerId = owner.id,
                                ownerName = owner.name,
                                ownerAvatarUrl = owner.avatarUrl,
                                title = title,
                                number = number,
                                state = state)

fun Collection<IssueJson>.toIssues() = map { it.toIssue() }

