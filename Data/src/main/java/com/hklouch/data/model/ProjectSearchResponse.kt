package com.hklouch.data.model

import com.google.gson.annotations.SerializedName

data class ProjectSearchResponse(@SerializedName("total_count") val totalCount: Int,
                                 @SerializedName("incomplete_results") val isIncompleteResult: Boolean,
                                 @SerializedName("items") val projects: List<ProjectJson>)