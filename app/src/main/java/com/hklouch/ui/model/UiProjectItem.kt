package com.hklouch.ui.model

import com.hklouch.domain.model.Project

class UiProjectItem(val id: String,
                    val url: String,
                    val name: String,
                    val fullName: String,
                    val ownerId: String,
                    val ownerName: String,
                    val ownerAvatarUrl: String,
                    val description: String?
)

fun Project.toUiProjectItem() = UiProjectItem(id = id,
                                              url = url,
                                              name = name,
                                              fullName = fullName,
                                              ownerId = ownerId,
                                              ownerName = ownerName,
                                              ownerAvatarUrl = ownerAvatarUrl,
                                              description = description)