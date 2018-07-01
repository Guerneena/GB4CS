package com.hklouch.domain.model

class Project(val id: String,
              val url: String,
              val name: String,
              val fullName: String,
              val ownerId: String,
              val ownerName: String,
              val ownerAvatarUrl: String,
              val description: String?,
              val collaboratorsUrl: String,
              val isFork: Boolean,
              val starCount: Int,
              val watchersCount: Int,
              val issuesUrl: String,
              val contributorsUrl: String,
              val branchesUrl: String
)