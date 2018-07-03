package com.hklouch.domain.model

data class ProjectPreview(val id: Int,
                          val name: String,
                          val fullName: String,
                          val ownerName: String,
                          val ownerAvatarUrl: String,
                          val description: String,
                          val isFork: Boolean,
                          val starsCount: Int
)
