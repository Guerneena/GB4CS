package com.hklouch.data.network.issue

import com.google.gson.annotations.SerializedName
import com.hklouch.data.network.user.UserJson

data class IssueJson(@SerializedName("url") val url: String,
                     @SerializedName("user") val owner: UserJson,
                     @SerializedName("title") val title: String,
                     @SerializedName("number") val number: Int,
                     @SerializedName("state") val state: String
)
