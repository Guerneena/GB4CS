package com.hklouch.data.model

import com.google.gson.annotations.SerializedName

data class ProjectJson(@SerializedName("id") val id: Int,
                  @SerializedName("url") val url: String,
                  @SerializedName("name") val name: String,
                  @SerializedName("full_name") val fullName: String,
                  @SerializedName("owner") val owner: OwnerJson,
                  @SerializedName("description") val description: String,
                  @SerializedName("collaborators_url") val collaboratorsUrl: String,
                  @SerializedName("fork") val isFork: Boolean,
                  @SerializedName("stargazers_count") val starCount: Int,
                  @SerializedName("watchers_count") val watchersCount: Int,
                  @SerializedName("issues_url") val issuesUrl: String,
                  @SerializedName("contributors_url") val contributorsUrl: String,
                  @SerializedName("branches_url") val branchesUrl: String
)