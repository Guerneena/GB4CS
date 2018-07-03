package com.hklouch.ui.model

import com.hklouch.domain.model.Project


data class UiProjectItem(val project: Project,
                         val id: Int,
                         val url: String,
                         val name: String,
                         val fullName: String,
                         val ownerId: String,
                         val ownerName: String,
                         val ownerAvatarUrl: String,
                         val description: String?
)


fun Project.toUiProjectItem() = UiProjectItem(project = this,
                                              id = id,
                                              url = url,
                                              name = name,
                                              fullName = fullName,
                                              ownerId = ownerId,
                                              ownerName = ownerName,
                                              ownerAvatarUrl = ownerAvatarUrl,
                                              description = description)