package com.hklouch.domain.model

data class Project(val id: Int,
                   val url: String,
                   val name: String,
                   val fullName: String,
                   val ownerId: Int,
                   val ownerName: String,
                   val ownerAvatarUrl: String,
                   val description: String?,
                   val collaboratorsUrl: String?,
                   val isFork: Boolean,
                   val forksCount: Int?,
                   val issuesCount: Int?,
                   val starsCount: Int?,
                   val watchersCount: Int?,
                   val issuesUrl: String?,
                   val contributorsUrl: String?,
                   val branchesUrl: String?,
                   val pullsUrl: String?
)