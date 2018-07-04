package com.hklouch.data.model

import com.google.gson.annotations.SerializedName

data class OwnerJson(@SerializedName("id") val id: Int,
                @SerializedName("login") val name: String,
                @SerializedName("avatar_url") val avatarUrl: String
)