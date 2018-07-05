package com.hklouch.data.network.pull

import com.google.gson.annotations.SerializedName
import com.hklouch.data.network.user.UserJson

data class PullJson(@SerializedName("url") val url: String,
                    @SerializedName("user") val owner: UserJson,
                    @SerializedName("title") val title: String,
                    @SerializedName("number") val number: Int,
                    @SerializedName("state") val state: String
)
