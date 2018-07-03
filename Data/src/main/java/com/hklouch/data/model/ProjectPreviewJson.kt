package com.hklouch.data.model

import com.google.gson.annotations.SerializedName

data class ProjectPreviewJson(@SerializedName("id") val id: Int,
                              @SerializedName("name") val name: String,
                              @SerializedName("full_name") val fullName: String,
                              @SerializedName("owner") val owner: OwnerJson,
                              @SerializedName("description") val description: String,
                              @SerializedName("fork") val isFork: Boolean,
                              @SerializedName("stargazers_count") val starsCount: Int
)