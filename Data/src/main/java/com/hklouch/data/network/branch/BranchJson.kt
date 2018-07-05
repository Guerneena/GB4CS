package com.hklouch.data.network.branch

import com.google.gson.annotations.SerializedName

data class BranchJson(@SerializedName("name") val name: String,
                      @SerializedName("commit") val head: CommitJson) {

    data class CommitJson(@SerializedName("sha") val sha: String,
                          @SerializedName("url") val url: String)
}

