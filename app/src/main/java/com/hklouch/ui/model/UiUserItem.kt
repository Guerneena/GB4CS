package com.hklouch.ui.model

import com.hklouch.domain.model.User


data class UiUserItem(val id: Int,
                      val name: String,
                      val avatarUrl: String
)


fun User.toUiUserItem() = UiUserItem(id = id,
                                     name = name,
                                     avatarUrl = avatarUrl)