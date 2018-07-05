package com.hklouch.data.network.project

import com.google.gson.annotations.SerializedName
import com.hklouch.data.network.project.ProjectJson

data class ProjectSearchResponse(@SerializedName("total_count") val totalCount: Int,
                                 @SerializedName("incomplete_results") val isIncompleteResult: Boolean,
                                 @SerializedName("items") val projects: List<ProjectJson>)