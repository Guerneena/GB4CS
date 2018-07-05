package com.hklouch.domain.model

data class Pull(val url: String,
                val ownerId: Int,
                val ownerName: String,
                val ownerAvatarUrl: String,
                val title: String,
                val number: Int,
                val state: String
)
