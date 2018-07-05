package com.hklouch.data.network.project

import com.google.gson.annotations.SerializedName
import com.hklouch.data.network.user.UserJson

data class ProjectJson(@SerializedName("id") val id: Int,
                       @SerializedName("url") val url: String,
                       @SerializedName("name") val name: String,
                       @SerializedName("full_name") val fullName: String,
                       @SerializedName("owner") val owner: UserJson,
                       @SerializedName("description") val description: String,
                       @SerializedName("fork") val isFork: Boolean,
                       @SerializedName("forks_count") val forksCount: Int,
                       @SerializedName("open_issues_count") val issuesCount: Int,
                       @SerializedName("stargazers_count") val starsCount: Int,
                       @SerializedName("watchers_count") val watchersCount: Int,
                       @SerializedName("issues_url") val issuesUrl: String,
                       @SerializedName("contributors_url") val contributorsUrl: String,
                       @SerializedName("collaborators_url") val collaboratorsUrl: String,
                       @SerializedName("branches_url") val branchesUrl: String,
                       @SerializedName("pulls_url") val pullsUrl: String
)